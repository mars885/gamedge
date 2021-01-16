/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulrybitskyi.hiltbinder.processor

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects.forResource
import com.google.testing.compile.JavaFileObjects.forSourceLines
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources
import com.paulrybitskyi.hiltbinder.BindType
import com.paulrybitskyi.hiltbinder.processor.factories.FileInterfaceNameFactory
import com.paulrybitskyi.hiltbinder.processor.model.HiltComponent
import com.paulrybitskyi.hiltbinder.processor.model.WITH_FRAGMENT_BINDINGS_TYPE_CANON_NAME
import com.paulrybitskyi.hiltbinder.processor.providers.MessageProvider
import org.junit.Test

internal class HiltBinderTest {


    private companion object {

        private val COMPONENT_MAPPER = ComponentMapper()
        private val FILE_INTERFACE_NAME_FACTORY = FileInterfaceNameFactory()
        private val MESSAGE_PROVIDER = MessageProvider()

        private val VALID_ANNOTATION_COMPONENTS = BindType.Component
            .values()
            .filter { it != BindType.Component.NONE }

    }


    @Test
    fun `Binds class implicitly to its single interface`() {
        val returnType = forResource("Testable.java")
        val bindingType = forResource("1/Test.java")
        val expectedModule = forResource("1/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly to its single interface`() {
        val returnType = forResource("Testable.java")
        val bindingType = forResource("2/Test.java")
        val expectedModule = forResource("2/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class implicitly to its superclass`() {
        val returnType = forResource("AbstractTest.java")
        val bindingType = forResource("3/Test.java")
        val expectedModule = forResource("3/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly to its superclass`() {
        val returnType = forResource("AbstractTest.java")
        val bindingType = forResource("4/Test.java")
        val expectedModule = forResource("4/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly, which has two interfaces, to specific interface`() {
        val returnType = forResource("5/Testable1.java")
        val interfaceType = forResource("5/Testable2.java")
        val bindingType = forResource("5/Test.java")
        val expectedModule = forResource("5/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, interfaceType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly, which has superclass and interface, to superclass`() {
        val returnType = forResource("AbstractTest.java")
        val interfaceType = forResource("Testable.java")
        val bindingType = forResource("6/Test.java")
        val expectedModule = forResource("6/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, interfaceType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly, which has superclass and interface, to interface`() {
        val returnType = forResource("Testable.java")
        val superclassType = forResource("AbstractTest.java")
        val bindingType = forResource("7/Test.java")
        val expectedModule = forResource("7/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, superclassType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly to Object class`() {
        val bindingType = forResource("8/Test.java")
        val expectedModule = forResource("8/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly, which has superclass, to Object class`() {
        val superclassType = forResource("AbstractTest.java")
        val bindingType = forResource("9/Test.java")
        val expectedModule = forResource("9/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(superclassType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly, which has interface, to Object class`() {
        val interfaceType = forResource("Testable.java")
        val bindingType = forResource("10/Test.java")
        val expectedModule = forResource("10/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(interfaceType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly, which has superclass and interface, to Object class`() {
        val superclassType = forResource("AbstractTest.java")
        val interfaceType = forResource("Testable.java")
        val bindingType = forResource("11/Test.java")
        val expectedModule = forResource("11/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(superclassType, interfaceType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly, which has superclass, to its superclass's implemented interface`() {
        val returnType = forResource("Testable.java")
        val superclassType = forResource("12/AbstractTest.java")
        val bindingType = forResource("12/Test.java")
        val expectedModule = forResource("12/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, superclassType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly, which has superclass, to its superclass's superclass`() {
        val returnType = forResource("13/AbstractAbstractTest.java")
        val superclassType = forResource("13/AbstractTest.java")
        val bindingType = forResource("13/Test.java")
        val expectedModule = forResource("13/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, superclassType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class explicitly, which has interface, to its interface's base interface`() {
        val returnType = forResource("Testable.java")
        val interfaceType = forResource("14/UnitTestable.java")
        val bindingType = forResource("14/Test.java")
        val expectedModule = forResource("14/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, interfaceType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class implicitly to its parameterized interface`() {
        val returnType = forResource("15/Testable.java")
        val bindingType = forResource("15/Test.java")
        val expectedModule = forResource("15/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds class implicitly to its parameterized superclass`() {
        val returnType = forResource("16/AbstractTest.java")
        val bindingType = forResource("16/Test.java")
        val expectedModule = forResource("16/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Fails to bind class implicitly, which does not have superclass or interface`() {
        val bindingType = forResource("17/Test.java")

        assertAbout(javaSources())
            .that(listOf(bindingType))
            .processedWith(HiltBinderProcessor())
            .failsToCompile()
            .withErrorContaining(MESSAGE_PROVIDER.undefinedReturnTypeError())
            .`in`(bindingType)
            .onLine(4)
    }


    @Test
    fun `Fails to bind class implicitly, which has two interfaces`() {
        val interfaceTypes = listOf(
            forResource("18/Testable1.java"),
            forResource("18/Testable2.java")
        )
        val bindingType = forResource("18/Test.java")

        assertAbout(javaSources())
            .that(interfaceTypes + listOf(bindingType))
            .processedWith(HiltBinderProcessor())
            .failsToCompile()
            .withErrorContaining(MESSAGE_PROVIDER.undefinedReturnTypeError())
            .`in`(bindingType)
            .onLine(4)
    }


    @Test
    fun `Fails to bind class implicitly, which has superclass and interface`() {
        val superclassType = forResource("AbstractTest.java")
        val interfaceType = forResource("Testable.java")
        val bindingType = forResource("19/Test.java")

        assertAbout(javaSources())
            .that(listOf(superclassType, interfaceType, bindingType))
            .processedWith(HiltBinderProcessor())
            .failsToCompile()
            .withErrorContaining(MESSAGE_PROVIDER.undefinedReturnTypeError())
            .`in`(bindingType)
            .onLine(4)
    }


    @Test
    fun `Fails to bind class explicitly, which does not have superclass or interface, to interface`() {
        val interfaceType = forResource("Testable.java")
        val bindingType = forResource("20/Test.java")

        assertAbout(javaSources())
            .that(listOf(interfaceType, bindingType))
            .processedWith(HiltBinderProcessor())
            .failsToCompile()
            .withErrorContaining(MESSAGE_PROVIDER.noSubtypeRelationError("Test", "Testable"))
            .`in`(bindingType)
            .onLine(4)
    }


    @Test
    fun `Fails to bind class explicitly, which does not have superclass or interface, to class`() {
        val classType = forResource("AbstractTest.java")
        val bindingType = forResource("21/Test.java")

        assertAbout(javaSources())
            .that(listOf(classType, bindingType))
            .processedWith(HiltBinderProcessor())
            .failsToCompile()
            .withErrorContaining(MESSAGE_PROVIDER.noSubtypeRelationError("Test", "AbstractTest"))
            .`in`(bindingType)
            .onLine(4)
    }


    @Test
    fun `Installs binding in hilt component, which is deduced from scope annotation`() {
        val returnType = forResource("Testable.java")

        for(hiltComponent in HiltComponent.values()) {
            val isViewWithFragmentComponent = (hiltComponent == HiltComponent.VIEW_WITH_FRAGMENT)
            val withFragmentBindingAnnotation = if(isViewWithFragmentComponent) {
                "@$WITH_FRAGMENT_BINDINGS_TYPE_CANON_NAME"
            } else {
                ""
            }

            val bindingType = forSourceLines(
                "Test",
                """
                import com.paulrybitskyi.hiltbinder.BindType;
                
                @${hiltComponent.scopeName}
                $withFragmentBindingAnnotation
                @BindType
                public class Test implements Testable {}
            """.trimIndent()
            )
            val interfaceName = FILE_INTERFACE_NAME_FACTORY.createInterfaceName(hiltComponent)
            val expectedModule = forSourceLines(
                interfaceName,
                """
                // Generated by @BindType. Do not modify!
                
                import dagger.Binds;
                import dagger.Module;
                import dagger.hilt.InstallIn;
                import ${hiltComponent.typeName};
                
                @Module
                @InstallIn(${hiltComponent.title}.class)
                public interface $interfaceName {
                  @Binds
                  Testable bind_Test(Test binding);
                }
                """.trimIndent()
            )

            assertAbout(javaSources())
                .that(listOf(returnType, bindingType))
                .processedWith(HiltBinderProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedModule)
        }
    }


    @Test
    fun `Installs binding in hilt component, which is explicitly specified in annotation`() {
        val returnType = forResource("Testable.java")

        for(component in VALID_ANNOTATION_COMPONENTS) {
            val bindingType = forSourceLines(
                "Test",
                """
                import com.paulrybitskyi.hiltbinder.BindType;
                
                @BindType(installIn = BindType.Component.${component.name})
                public class Test implements Testable {}
            """.trimIndent()
            )
            val hiltComponent = COMPONENT_MAPPER.mapToHiltComponent(component)
            val interfaceName = FILE_INTERFACE_NAME_FACTORY.createInterfaceName(hiltComponent)
            val expectedModule = forSourceLines(
                interfaceName,
                """
                // Generated by @BindType. Do not modify!
                
                import dagger.Binds;
                import dagger.Module;
                import dagger.hilt.InstallIn;
                import ${hiltComponent.typeName};
                
                @Module
                @InstallIn(${hiltComponent.title}.class)
                public interface $interfaceName {
                  @Binds
                  Testable bind_Test(Test binding);
                }
                """.trimIndent()
            )

            assertAbout(javaSources())
                .that(listOf(returnType, bindingType))
                .processedWith(HiltBinderProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedModule)
        }
    }


    @Test
    fun `Fails to install binding in hilt component, when both scope and explicit specification is present`() {
        val interfaceType = forResource("Testable.java")

        for(component in VALID_ANNOTATION_COMPONENTS) {
            val hiltComponent = COMPONENT_MAPPER.mapToHiltComponent(component)
            val bindingType = forSourceLines(
                "Test",
                """
                import com.paulrybitskyi.hiltbinder.BindType;
                
                @${hiltComponent.scopeName}
                @BindType(installIn = BindType.Component.${component.name})
                public class Test implements Testable {}
            """.trimIndent()
            )

            assertAbout(javaSources())
                .that(listOf(interfaceType, bindingType))
                .processedWith(HiltBinderProcessor())
                .failsToCompile()
                .withErrorContaining(MESSAGE_PROVIDER.duplicatedComponentError())
                .`in`(bindingType)
                .onLine(5)
        }
    }


    @Test
    fun `Binds class with qualifier to its single interface`() {
        val returnType = forResource("Testable.java")
        val bindingType = forResource("22/Test.java")
        val expectedModule = forResource("22/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Fails to bind class, which is specified to have qualifier, but does not come have it`() {
        val interfaceType = forResource("Testable.java")
        val bindingType = forResource("23/Test.java")

        assertAbout(javaSources())
            .that(listOf(interfaceType, bindingType))
            .processedWith(HiltBinderProcessor())
            .failsToCompile()
            .withErrorContaining(MESSAGE_PROVIDER.qualifierAbsentError())
            .`in`(bindingType)
            .onLine(4)
    }


    @Test
    fun `Binds classes to multibound set`() {
        val returnType = forResource("Testable.java")
        val bindingTypes = listOf(
            forResource("24/Test1.java"),
            forResource("24/Test2.java"),
            forResource("24/Test3.java")
        )
        val expectedModule = forResource("24/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType) + bindingTypes)
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds classes to qualified multibound set`() {
        val returnType = forResource("Testable.java")
        val bindingTypes = listOf(
            forResource("25/Test1.java"),
            forResource("25/Test2.java"),
            forResource("25/Test3.java")
        )
        val expectedModule = forResource("25/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType) + bindingTypes)
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Fails to bind class, which does not have @MapKey annotation, to multibound map `() {
        val returnType = forResource("Testable.java")
        val bindingType = forResource("26/Test.java")

        assertAbout(javaSources())
            .that(listOf(returnType, bindingType))
            .processedWith(HiltBinderProcessor())
            .failsToCompile()
            .withErrorContaining(MESSAGE_PROVIDER.noMapKeyError())
            .`in`(bindingType)
            .onLine(4)
    }


    @Test
    fun `Binds classes to multibound map using standard integer key annotation`() {
        val returnType = forResource("Testable.java")
        val bindingTypes = listOf(
            forResource("27/Test1.java"),
            forResource("27/Test2.java"),
            forResource("27/Test3.java")
        )
        val expectedModule = forResource("27/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType) + bindingTypes)
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds classes to multibound map using standard long key annotation`() {
        val returnType = forResource("Testable.java")
        val bindingTypes = listOf(
            forResource("28/Test1.java"),
            forResource("28/Test2.java"),
            forResource("28/Test3.java")
        )
        val expectedModule = forResource("28/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType) + bindingTypes)
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds classes to multibound map using standard string key annotation`() {
        val returnType = forResource("Testable.java")
        val bindingTypes = listOf(
            forResource("29/Test1.java"),
            forResource("29/Test2.java"),
            forResource("29/Test3.java")
        )
        val expectedModule = forResource("29/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType) + bindingTypes)
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds classes to multibound map using standard class key annotation`() {
        val returnType = forResource("Testable.java")
        val bindingTypes = listOf(
            forResource("30/Test1.java"),
            forResource("30/Test2.java"),
            forResource("30/Test3.java")
        )
        val expectedModule = forResource("30/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType) + bindingTypes)
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds classes to multibound map using custom @MapKey annotation`() {
        val returnType = forResource("Testable.java")
        val customMapKey = forResource("31/TestMapKey.java")
        val bindingTypes = listOf(
            forResource("31/Test1.java"),
            forResource("31/Test2.java"),
            forResource("31/Test3.java")
        )
        val expectedModule = forResource("31/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, customMapKey) + bindingTypes)
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Binds classes to qualified multibound map`() {
        val returnType = forResource("Testable.java")
        val bindingTypes = listOf(
            forResource("32/Test1.java"),
            forResource("32/Test2.java"),
            forResource("32/Test3.java")
        )
        val expectedModule = forResource("32/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType) + bindingTypes)
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Verify that binding method is properly formatted`() {
        val returnType = forResource("33/Testable.java")
        val bindingType = forResource("33/Test.java")
        val expectedModule = forResource("33/ExpectedModule.java")

        assertAbout(javaSources())
            .that(listOf(returnType, bindingType))
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


    @Test
    fun `Verify that common prefix package name is used based on bindings of hilt component`() {
        val returnTypes = listOf(
            forResource("34/Testable1.java"),
            forResource("34/Testable2.java"),
            forResource("34/Testable3.java")
        )
        val bindingTypes = listOf(
            forResource("34/Test1.java"),
            forResource("34/Test2.java"),
            forResource("34/Test3.java")
        )
        val expectedModule = forResource("34/ExpectedModule.java")

        assertAbout(javaSources())
            .that(returnTypes + bindingTypes)
            .processedWith(HiltBinderProcessor())
            .compilesWithoutError()
            .and()
            .generatesSources(expectedModule)
    }


}