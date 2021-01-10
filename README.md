---
layout: post
title: Scope Functions in Kotlin
description: Scope Functions in Kotlin
---
# Purpose

While I have always been interested in Kotlin, I am now starting to use it professionally. Like most languages, there are many things that I like, and a few things that I dislike about Kotlin. Over the next few blog posts, I plan to describe features with in the language that I find beneficial, and some that are confusing and/or annoying.  

# Scope Functions

Scope functions have been a pleasant surprise. Scope functions in Kotlin provide the ability to execute a block of code within the context of an object. This does sound trivial, but let me provide some examples that I hope will clarify their capabilities. 

There are five scope functions: `let`, `run`, `with`, `apply`, and `also`. Each function is slightly different in object reference and return value. The table below provides an overview of the differences between the functions. Next, I will go over each of these functions in more detail.

### Function Selection 

|Function	| Object reference | Return value   | Is extension function
|-----------|------------------|----------------|----------------------
| let       | it               | Lambda result  | Yes 
| run       | this             | Lambda result  | Yes
| run       | -                | Lambda result  | No: called without the context object
| with      | this             | Context object | No: takes the context object as an argument
| apply     | this             | Context object | Yes
| also      | it               | Context object | Yes

Kotlin's website defines the functional selection using the previous table. See: [function-selection](https://kotlinlang.org/docs/reference/scope-functions.html#function-selection)

## Let Function

Kotlin software commonly uses `let` function for executing lambdas on non-null objects and for introducing an expression as a variable in local scope. 


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

The previous example executes the `let` function on a list of integers. The lambda then interments the count the list using the `it` variable and returns the resulting count. This is a contrived example, but hopefully it shows the capability of the `let` function.  The `let` function is probably more commonly used to execute a lambda on a non-null object.

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

In the previous example, the code uses the safe operator (i.e., `?.`) in Kotlin in combination with the `let` function to execute a lambda on a non-null object. The resulting message variable being "Hello World!".

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

In the previous example, the lambda for the `let` function isn't executed resulting in the `message` variable being null. 


### Run

The `run` function is Kotlin is commonly used for object creation and computing a result. A good example for this would be instantiating a service, configuring the properties (e.g., ports), executing the service, and return the results.

```kotlin
val service = HttpService()

val result = service.run {
    port = 8888
    context = "/acme"
    fetchResults()
}
```

The following is another contrived unit test can be used to experiment wth the `run` function.

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


### With

The `with` function isn't an extension function, meaning it is just a normal function that takes the context object as a parameter and returns the result of the lambda. 

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

The previous example is simple example of using the `with` function. The `with` function is commonly used to group function calls on an object.

### Apply 

The `apply` function is that provides `this` as an object reference and returns the context object. The `apply` function is commonly used to configure objects. 


```kotlin
val mongoDBContainer = MongoDBContainer().apply{
    withExposedPorts(27017) 
    start()
}
```

The previous example creates an instance of `MongoDBContainer` ([TestContainer](https://www.testcontainers.org/modules/databases/mongodb/)) and calls the `apply` function on the object. The `apply` function scopes the context of the lambda to the `MongoDBContainer` object. 

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

The previous example is a unit test more easily be used to experiment with the `apply` scope function. This a contrived example, but it demonstrates creating a list, adding integers to the list, and returning the list.  

### Also

The `also` function is a scope function is commonly used to provide additional effects to an object. The `also` function provides the context object as the argument `it` and returns the context object.   

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

These functions are great for providing temporary scope and very useful in improving readability of code. I find code that limits scope and mutability is easier for me to read. Scope functions definitely helps to limit scope and therefore improves readability for me. I hope this blog post has provided some insight into _scope functions_ within Kotlin. If you want to experiment with any of the examples, they can be found [here](https://github.com/seanking/scope-functions-kotlin) in GitHub.
