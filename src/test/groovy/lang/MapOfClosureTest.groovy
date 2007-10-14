/*
 * Copyright 2003-2007 the original author or authors.
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
package groovy.lang

/**
 * Tests maps of closures coerced to classes by asType()
 *
 * @author Jochen Theodorou
 * @author Guillaume Laforge
 */
class MapOfClosureTest extends GroovyTestCase {

    void testInterfaceProxy() {
        def outer = 1
        def x = [run: { outer++ }] as Runnable
        x.run()

        assert x instanceof Runnable
        assert outer == 2
    }

    void testObject() {
        def m = [bar: { "foo" }]
        def x = m as Object

        assert x.is(m)
        assert "foo" == x.bar()
    }

    void testAbstractClassSubclassing() {
        def outer = 1
        def x = [run: { outer++ }] as TimerTask
        x.run()
        assert x instanceof TimerTask
        assert outer == 2
    }

    /**
     * Checks public and protected methods from parents can also be overriden by the Map coercion to classes.
     */
    void testOverrideProtectedMethods() {
        def b = [pub: { "map pub" }, prot: { "map prot" }, child: { "map child" }] as B

        assert "map pub" == b.pub()
        assert "map prot" == b.prot()
        assert "map child" == b.child()
        assert "abstract" == b.abstractMethod()
    }

    /**
     * Checks that abstract methods can also be overriden.
     */
    void testAbstractMethodIsOverrided() {
        def a = [abstractMethod: { "map abstract" }] as A

        assert "map abstract" == a.abstractMethod()
    }

    /**
     * Verify that complex method signatures, even with primitive types and arrays, can be overriden.
     */
    void testComplexMethodSignature() {
        def c = [foo: { int a, List b, Double[] c -> ["map foo"] as String[] }] as C

        assert ["map foo"] as String[] == c.foo(1, ['a', 'b'], [0.2, 0.3] as Double[])
    }
}

abstract class A {
    protected prot() { "prot" }
    def pub() { "pub" }
    abstract abstractMethod()
}

class B extends A {
    protected child() { "child" }
    def abstractMethod() { "abstract" }
}

class C {
    String[] foo(int a, List b, Double[] c) { ["foo"] as String[] }
}