/*
 * Copyright 2015 Intershop Communications AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.intershop.gradle.wsdl

import com.intershop.gradle.test.AbstractProjectSpec
import com.intershop.gradle.wsdl.extension.WSDLExtension
import com.intershop.gradle.wsdl.utils.DeployScope
import org.gradle.api.Plugin

class WSDLPluginSpec extends AbstractProjectSpec {

    @Override
    Plugin getPlugin() {
        return new WSDLPlugin()
    }

    def 'should add extension named wsdl'() {
        when:
        plugin.apply(project)

        then:
        project.extensions.getByName(WSDLExtension.WSDL_EXTENSION_NAME)
    }

    def 'should add WSDL generate task for each wsdl config'() {
        when:
        plugin.apply(project)
        project.extensions.getByName(WSDLExtension.WSDL_EXTENSION_NAME).axis1 {
            testconfiguration {
            }
        }

        then:
        project.tasks.findByName("axis1Wsdl2javaTestconfiguration")

        when:
        project.extensions.getByName(WSDLExtension.WSDL_EXTENSION_NAME).axis2 {
            testconfiguration {
            }
        }

        then:
        project.tasks.findByName("axis2Wsdl2javaTestconfiguration")
    }

    def 'should set all parameters in task axis1'() {
        when:
        plugin.apply(project)
        project.extensions.getByName(WSDLExtension.WSDL_EXTENSION_NAME).axis1 {
            testconfiguration {
                noImports = true
                timeout = 360
                noWrapped = true
                serverSide = true
                skeletonDeploy = true
                deployScope = 'Application'
                generateAllClasses = true
                typeMappingVersion = '1.2'
                factory = 'TestFactory'
                helperGen = true
                userName = 'test'
                password = 'testp'
                implementationClassName = 'BlaClassName'
                wrapArrays = true
                sourceSetName = 'test'
                packageName = 'com.test'
                generateTestcase = true
                namespacePackageMappings {
                    conf1 {
                        namespace = "a"
                        packageName = "com.intershop.a"
                    }
                    conf2 {
                        namespace = "b"
                        packageName = "com.intershop.b"
                    }
                    conf3 {
                        namespace = "c"
                        packageName = "com.intershop.c"
                    }
                }
                namespacePackageMappingFile = project.file('wsdl/package.mapping')
                wsdlFile = project.file('MyFile.wsdl')
                args = ['-Dtest=test', '-Dtest1=test1'] as List<String>
            }
        }
        com.intershop.gradle.wsdl.tasks.axis1.WSDL2Java task = project.tasks.findByName("axis1Wsdl2javaTestconfiguration") as com.intershop.gradle.wsdl.tasks.axis1.WSDL2Java

        then:
        task
        task.noImports
        task.timeoutConf == 360
        task.noWrapped
        task.serverSide
        task.skeletonDeploy
        task.deployScope == DeployScope.APPLICATION.scope
        task.generateAllClasses
        task.typeMappingVersion == '1.2'
        task.factory == 'TestFactory'
        task.helperGen
        task.userName == 'test'
        task.password == 'testp'
        task.implementationClassName == 'BlaClassName'
        task.wrapArrays
        task.packageName == 'com.test'
        task.generateTestcase
        task.namespacePackageMappingList.contains("a=com.intershop.a")
        task.namespacePackageMappingFile == project.file('wsdl/package.mapping')
        task.wsdlFile == project.file('MyFile.wsdl')
        task.args ==  ['-Dtest=test', '-Dtest1=test1'] as List<String>
    }

    def 'should set all parameters in task axis2'() {
        when:
        plugin.apply(project)
        project.extensions.getByName(WSDLExtension.WSDL_EXTENSION_NAME).axis2 {
            testconfiguration {
                wsdlFile = project.file('MyFile.wsdl')
                packageName = 'com.intershop.wsdl'
                namespacePackageMappings {
                    conf1 {
                        namespace = "a"
                        packageName = "com.intershop.a"
                    }
                    conf2 {
                        namespace = "b"
                        packageName = "com.intershop.b"
                    }
                    conf3 {
                        namespace = "c"
                        packageName = "com.intershop.c"
                    }
                }
                namespacePackageMappingFile = project.file('wsdl/package.mapping')
                unwrapParams = true
                generateAllClasses = true
                addArg('testParameter')

            }
        }
        com.intershop.gradle.wsdl.tasks.axis2.WSDL2Java task = project.tasks.findByName("axis2Wsdl2javaTestconfiguration") as com.intershop.gradle.wsdl.tasks.axis2.WSDL2Java

        then:
        task
        task.wsdlFile == project.file('MyFile.wsdl')
        task.packageName == 'com.intershop.wsdl'
        task.namespacePackageMappingFile == project.file('wsdl/package.mapping')
        task.unwrapParams
        task.generateAllClasses
        task.wsdlVersion == ""
        task.args == ['testParameter'] as List<String>
    }
}
