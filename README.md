---
layout: post
title: Scope Functions in Kotlin
description: Scope Functions in Kotlin
---
# Purpose

While I have always been interested in the [Kotlin](https://kotlinlang.org) programming language, I am now starting to use it professionally. Like most languages, there are many things that I like, and a few things that I dislike about Kotlin. Over the next few blog posts, I plan to describe features with in the language that I find beneficial, and some that are confusing and/or annoying.

# Scope Functions

Scope functions have been a pleasant surprise. They provide the ability to execute a block of code within the context of an object. This sound trivial, but let me provide some examples that I hope will clarify their capabilities.

There are five scope functions: `let`, `run`, `with`, `apply`, and `also`. Each function is slightly different in the object reference for the lambda and return value of the function. The table below provides an overview of the differences between the functions. The table below describes is an overview of the functions' similarities and differences.

### Function Selection 

|Function	| Object reference | Return value   | Is extension function
|-----------|------------------|----------------|----------------------
| let       | it               | Lambda result  | Yes 
| run       | this             | Lambda result  | Yes
| run       | -                | Lambda result  | No: called without the context object
| with      | this             | Context object | No: takes the context object as an argument
| apply     | this             | Context object | Yes
| also      | it               | Context object | Yes

Kotlin's website defines the above functional selection table. See: [Function Selection](https://kotlinlang.org/docs/reference/scope-functions.html#function-selection)

## Let Function

Kotlin software commonly uses the `let` function for executing lambdas on non-null objects and for introducing an expression as a variable in the local scope. 


```kotlin
@Test
fun `should use _it_ as object reference and return lambda result using _let_ function`() {
    // Given
    val numbers = mutableListOf<Int>()

    // When
    val count = numbers.let {
        it.add(1)
        it.add(2)
        it.count()
    }

    // Then
    assertThat(count).isEqualTo(2)
}
```

The previous example executes the `let` function on a list of integers. The lambda adds to the list using the `it` variable and returns the resulting count. This is a contrived example, but hopefully it shows the capability of the `let` function.  The `let` function is probably more commonly used to execute lambdas on non-null objects.

```kotlin
@Test
fun `should execute code block for non-null values`() {
    // Given 
    val optionalVal: String? = "Hello"
    
    // When
    val message = optionalVal?.let { "$it World!" }
    
    // Then
    assertThat(message).isNotNull().isEqualTo("Hello World!")
}
```

In the previous example, the code uses the safe operator (i.e., `?.`) in combination with the `let` function to execute a lambda on a non-null object. This results in the message variable being "Hello World!".

```kotlin
@Test
fun `should not execute code block for non-null values`() {
    // Given 
    val optionalVal: String? = null

    // When
    val message = optionalVal?.let { "This shouldn't be called." }

    // Then
    assertThat(message).isNull()
}
```

In the previous example, the lambda for the `let` function isn't executed because the `optionalVal` was null. This results in the `message` variable being null. 


### Run

The `run` function is commonly used for object creation and computing a result. A good example for this would be instantiating a service, configuring the properties (e.g., ports), executing the service, and returning the results.

```kotlin
val service = HttpService()

val result = service.run {
    port = 8888
    context = "/acme"
    fetchResults()
}
```

The following is another contrived unit test. It can be used to experiment wth the `run` function.

```kotlin
@Test
fun `should use _this_ as object reference and return lambda result using _run_ function`() {
    // Given
    val numbers = mutableListOf<Int>()

    // When
    val count = numbers.run {
        add(1)
        add(2)
        count()
    }

    // Then
    assertThat(count).isEqualTo(2)
}
```

The previous example executes the `run` function on a list of integers. The lambda adds to the list using the list as the context and returns the resulting count. This is another contrived example, but hopefully it shows the capability of the `run` function.

### With

The `with` function isn't an extension function, meaning it a regular function that takes the context object as a parameter and returns the result of the lambda. 

```kotlin
@Test
fun `should use _this_ as object reference and return lambda result using _with_ function`() {
    // Given
    val numbers = mutableListOf<Int>(1, 2)

    // When
    val count = with(numbers) {
        count()
    }

    // Then
    assertThat(count).isEqualTo(2)
}
```

The previous example is a simple example of using the `with` function. The `with` function was called with the `numbers` variable, therefore scoping the following calls to `numbers`. The `with` function is commonly used to group function calls on an object.

### Apply 

The `apply` function provides `this` as an object reference and returns the context object. The `apply` function is commonly used to configure objects. 

```kotlin
val mongoDBContainer = MongoDBContainer().apply{
    withExposedPorts(27017) 
    start()
}
```

The previous example configures a [Mongo TestContainer](https://www.testcontainers.org/modules/databases/mongodb/) using the `apply` function. It creates an instance of `MongoDBContainer` and calls the `apply` function on the instantiated object. The `apply` function scopes the context of the lambda to the `MongoDBContainer` object and returns the object. 

```kotlin
 @Test
fun `should use _this_ as object reference and return context object using _apply_ function`() {
    // When
    val numbers = mutableListOf<Int>().apply {
        add(1)
        add(2)
    }

    // Then
    assertThat(numbers).containsExactly(1, 2)
}
```

The previous example is a unit test more easily be used to experiment with the `apply` scope function. This an another contrived example, but it demonstrates instantiating a list, adding integers the list, and returning the list.  

### Also

The `also` function is a scope function that is commonly used to provide additional effects to an object. The `also` function provides the context object as the argument `it` and returns the context object.   

```kotlin
@Test
fun `should use _it_ as object reference and return context object using _also_ function`() {
    // When
    val numbers = mutableListOf<Int>().also {
        it.add(1)
        it.add(2)
    }

    // Then
    assertThat(numbers).containsExactly(1, 2)
}
```

This example is another is similar to the `apply` function, but the context object is referencable as `it`.  

## Summary

Scope functions are great for providing temporary scope and very useful in improving readability of code. I find code that limits scope and mutability is easier for me to read. Scope functions definitely helps to limit scope and therefore improves readability for me. I hope this blog post has provided some insight into _scope functions_ within Kotlin. If you want to experiment with any of the examples, they can be found [here](https://github.com/seanking/scope-functions-kotlin) in GitHub.
