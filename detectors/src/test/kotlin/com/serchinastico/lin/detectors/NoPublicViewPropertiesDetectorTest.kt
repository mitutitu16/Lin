package com.serchinastico.lin.detectors

import com.serchinastico.lin.test.LintTest
import com.serchinastico.lin.test.LintTest.Expectation.NoErrors
import com.serchinastico.lin.test.LintTest.Expectation.SomeError
import org.junit.Test

class NoPublicViewPropertiesDetectorTest : LintTest {

    override val issue = NoPublicViewPropertiesDetector.issue

    @Test
    fun inJavaClass_whenViewFieldIsPrivate_detectsNoErrors() {
        expect(
            """
                |package foo;
                |
                |import android.view.View;
                |
                |class TestClass {
                |   private View view;
                |}
            """.inJava
        ) toHave NoErrors
    }

    @Test
    fun inJavaClass_whenViewFieldIsNonPrivate_detectsErrors() {
        expect(
            """
                |package foo;
                |
                |import android.view.View;
                |
                |class TestClass {
                |   View view;
                |}
            """.inJava
        ) toHave SomeError("src/foo/TestClass.java")
    }

    @Test
    fun inJavaClass_whenViewFieldIsOverriding_detectsNoErrors() {
        expect(
            """
                |package foo;
                |
                |import java.lang.Override;
                |import android.view.View;
                |
                |class TestClass {
                |   @Override View view;
                |}
            """.inJava
        ) toHave NoErrors
    }

    @Test
    fun inKotlinClass_whenViewFieldIsPrivate_detectsNoErrors() {
        expect(
            """
                |package foo
                |
                |import android.view.View
                |
                |class TestClass {
                |   private val view: View = View()
                |}
            """.inKotlin
        ) toHave NoErrors
    }

    @Test
    fun inKotlinClass_whenViewFieldIsNotPrivate_detectsErrors() {
        expect(
            """
                |package foo
                |
                |import android.view.View
                |
                |class TestClass {
                |   public val view: View = View(null)
                |}
            """.inKotlin
        ) toHave SomeError("src/foo/TestClass.kt")
    }

    @Test
    fun inKotlinClass_whenViewFieldIsOverriding_detectsErrors() {
        expect(
            """
                |package foo
                |
                |import android.view.View
                |
                |class TestClass {
                |   public override val view: View = View(null)
                |}
            """.inKotlin
        ) toHave NoErrors
    }
}