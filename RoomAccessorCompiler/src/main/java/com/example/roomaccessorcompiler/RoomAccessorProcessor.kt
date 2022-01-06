package com.example.roomaccessorcompiler

import androidx.room.Dao
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("androidx.room.Dao")
class RoomAccessorProcessor :AbstractProcessor(){

    companion object {
        private const val PACKAGE_NAME = "com.example.accountmodule"
    }

    private var messager: Messager?= null
    private var elementUtil: Elements? = null
    private var filer: Filer? = null
    private var generated = false

    private var className = ""


    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        elementUtil = processingEnv.elementUtils
        messager = processingEnv.messager

        if (processingEnv.options.containsKey("ROOM_ACCESSOR_NAME_PREFIX")) {
            className = "${processingEnv.options.get("ROOM_ACCESSOR_NAME_PREFIX")}RoomAccessor"
        } else {
            messager?.printMessage(Diagnostic.Kind.ERROR, "ROOM_ACCESSOR_NAME_PREFIX must be defined.")
            throw IllegalArgumentException()
        }
        if (className.isNullOrEmpty()) {
            messager?.printMessage(Diagnostic.Kind.ERROR, "ROOM_ACCESSOR_NAME_PREFIX must be defined.")
            throw IllegalArgumentException()
        }

        println(" ----------- AutoRoomAccessorProcessor ----------- ")
        println(" ----------- className = $className ----------- ")
    }


    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (generated || filer == null) {
            return true
        }
        val daoFunList = mutableListOf<FunSpec>()
        val interfaceFuns = mutableListOf<FunSpec>()
        for(element in roundEnv.getElementsAnnotatedWith(Dao::class.java)){

            val daoClassname = element.simpleName.toString()
            println("find DAO = $daoClassname")
            /**
             * .copy(nullable = false)  :NewsDetailDAO
             * .copy(nullable = true)  :NewsDetailDAO?
             */
            val daoTypeName = ClassName(elementUtil?.getPackageOf(element).toString(),daoClassname).copy(nullable = false)

            /**
             * internal fun getUserDao(): UserDao {
                  if (onGetDaoCallback == null) {
                     throw IllegalArgumentException("onGetDaoCallback must not be null!!")
                  }
                 return onGetDaoCallback!!.onGetUserDao()
              }
             */
            daoFunList.add(
                FunSpec.builder("get${daoClassname}")
                    .addModifiers(KModifier.INTERNAL)
                    .returns(daoTypeName)
                    .addCode("if (onGetDaoCallback == null) {\n" +
                            "throw IllegalArgumentException(\"onGetDaoCallback must not be null!!\")\n" +
                            "}\n" +
                            "return onGetDaoCallback!!.onGet$daoClassname()\n")
                    .build()
            )

            /**
             * interface OnGetDaoCallback {
                   fun onGetUserDao(): UserDao
               }
             */
            interfaceFuns.add(
                FunSpec.builder("onGet$daoClassname")
                    .addModifiers(KModifier.ABSTRACT)
                    .returns(daoTypeName)
                    .build()
            )
        }
        val infoClazzBuilder: TypeSpec.Builder = TypeSpec.objectBuilder(className)
            .addFunctions(daoFunList)

        buildCallback(className, interfaceFuns, infoClazzBuilder)

        //生成文件
        val javaFile = FileSpec
            .builder(PACKAGE_NAME, className)
            .addType(infoClazzBuilder.build())
            .build()

        try {
            javaFile.writeTo(filer!!)
            generated = true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return true
    }

    private fun buildCallback(className: String, interfaceFuns: MutableList<FunSpec>, builder: TypeSpec.Builder) {
        // 内部接口类
        val interfaceInnerClass: TypeSpec = TypeSpec.interfaceBuilder("OnGetDaoCallback")
            .addFunctions(interfaceFuns)
            .build()

        //根据接口类型获取接口名
        val onGetDaoCallbackType: TypeName = ClassName("${PACKAGE_NAME}.${className}", interfaceInnerClass.name!!)

        //var onGetDaoCallback: OnGetDaoCallback? = null
        //True to create a var instead of a val.
        val onGetDaoCallbackProperty = PropertySpec.builder("onGetDaoCallback", onGetDaoCallbackType.copy(nullable = true))
            .mutable(true)
            .initializer("null")
            .build()

        builder
            .addType(interfaceInnerClass)
            .addProperty(onGetDaoCallbackProperty)
    }

}