---
documentclass: extarticle
fontsize: 12pt
title: Documentation - Arrow
author: Sanjay Sankaran
dark: true
---

\maketitle
\newpage

# Arrow
Arrow is a general purpose high level scripting language, with loose typing. The arrow interpreter is made in Java.

# Hello World

a quick hello world would be:
```
[print] <- "Hello World"
```

# Syntax
In arrow, everything is an expression, including the entire program itself. Thus, each line (except the last one) is ended with a comma (`,`).

Here is an example to echo an input by a user:
```
[print] <- "Enter something",
value <- [input] <- null,
[print] <- "You said " + value
```
As you can see, the commas make the program seem like an expression.

## Operators

The operators in Arrow are as follows:

Numeric Operations:

* `a + b` (Numeric Addition)
* `a - b` (Numeric Subtraction)
* `a * b` (Numeric Multiplication)
* `a / b` (Numeric Division)

String Operations

* `a + b` (String Concatenation)

Call/Assignment Operator (`<-`)

* `a <- value` (Variable Initialisation/Assignment)
* `return_vals <- (func) <- args` (Function call)

## Literals

Arrow currently supports the following literals:

* String: `"Value"`
* Numeric: `123`, `456.789`, `.23`
* Null: `NULL` or `null`

# Future

The following will be implemented in the future:

* Control Flow (if and while statements)
* Function declarations
* Array/List types
* Logical and Boolean operators
