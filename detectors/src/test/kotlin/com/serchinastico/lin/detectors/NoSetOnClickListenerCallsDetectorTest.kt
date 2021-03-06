package com.serchinastico.lin.detectors

import com.serchinastico.lin.test.LintTest
import com.serchinastico.lin.test.LintTest.Expectation.NoErrors
import com.serchinastico.lin.test.LintTest.Expectation.SomeError
import org.junit.Test

class NoSetOnClickListenerCallsDetectorTest : LintTest {

    override val issue = NoSetOnClickListenerCallsDetector.issue

    @Test
    fun inJavaClass_whenCallIsNotSetOnClickListener_detectsNoErrors() {
        expect(
            """
                |package foo;
                |
                |import android.view.View;
                |
                |class TestClass implements View.OnClickListener {
                |   private View view;
                |
                |   public void main(String[] args) {
                |       view.doNotSetOnClickListener(this);
                |   }
                |}
            """.inJava
        ) toHave NoErrors
    }

    @Test
    fun inJavaClass_whenCallIsSetOnClickListener_detectsError() {
        expect(
            """
                |package foo;
                |
                |import android.view.View;
                |
                |class TestClass implements View.OnClickListener {
                |   private View view;
                |
                |   public void main(String[] args) {
                |       view.setOnClickListener(this);
                |   }
                |}
            """.inJava
        ) toHave SomeError("src/foo/TestClass.java")
    }

    @Test
    fun inJavaClass_whenCallIsNotAndroidViewSetOnClickListener_detectsNoErrors() {
        expect(
            """
                |package foo;
                |
                |class TestClass implements View.OnClickListener {
                |   public void main(String[] args) {
                |       setOnClickListener(this);
                |   }
                |
                |   private void setOnClickListener(View.OnClickListener listener) {}
                |}
            """.inJava
        ) toHave NoErrors
    }

    @Test
    fun inKotlinClass_whenCallIsNotSetOnClickListener_detectsNoErrors() {
        expect(
            """
                |package foo
                |
                |import android.view.View
                |
                |class TestClass: View.OnClickListener {
                |   private lateinit var view: View
                |
                |   public fun main(args: Array<String>) {
                |       view.doNotSetOnClickListener(this)
                |   }
                |}
            """.inKotlin
        ) toHave NoErrors
    }

    @Test
    fun inKotlinClass_whenCallIsSetOnClickListener_detectsError() {
        expect(
            """
                |package foo
                |
                |import android.view.View
                |
                |class TestClass: View.OnClickListener {
                |   private lateinit var view: View
                |
                |   public fun main(args: Array<String>) {
                |       view.setOnClickListener(this)
                |   }
                |}
            """.inKotlin
        ) toHave SomeError("src/foo/TestClass.kt")
    }

    @Test
    fun inKotlinClass_whenCallIsNotAndroidViewSetOnClickListener_detectsNoErrors() {
        expect(
            """
                |package foo
                |
                |class TestClass: View.OnClickListener {
                |   public fun main(args: Array<String>) {
                |       setOnClickListener(this)
                |   }
                |
                |   private fun setOnClickListener(listener: View.OnClickListener) {}
                |}
            """.inKotlin
        ) toHave NoErrors
    }
}