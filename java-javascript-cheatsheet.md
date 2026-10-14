# Java ↔ JavaScript Cheatsheet

> **Goal:** "I know how to do this in JavaScript. What's the Java way?"

## 1. Variables & Types

| JavaScript | Java |
|---|---|
| `let age = 25` | `int age = 25;` |
| `const name = "Pawan"` | `final String name = "Pawan";` |
| `let active = true` | `boolean active = true;` |
| Dynamic typing | Static typing |
| `undefined` | No direct equivalent; usually `null` means "no object/value" |

```js
let age = 25;
const name = "Pawan";
```

```java
int age = 25;
final String name = "Pawan";
```

## 2. Common Types

| JavaScript | Java |
|---|---|
| `number` | `int`, `long`, `double`, etc. |
| `string` | `String` |
| `boolean` | `boolean` |
| `null` | `null` |
| `undefined` | No direct equivalent |
| object | class/object |
| `Array` | array / `ArrayList` |

Java primitives:

```java
int
long
double
float
boolean
char
```

Wrapper objects:

```java
Integer
Long
Double
Boolean
Character
```

## 3. Strings

```js
const name = "Pawan";
name.toUpperCase();
name.length;
```

```java
String name = "Pawan";
name.toUpperCase();
name.length();
```

Java `String` is immutable [its contents cannot be changed after creation].

For repeated string building:

```java
StringBuilder builder = new StringBuilder();
builder.append("Hello ");
builder.append("Pawan");
String result = builder.toString();
```

## 4. Arrays

```js
const numbers = [10, 20, 30];
numbers.push(40);
console.log(numbers[0]);
console.log(numbers.length);
```

```java
int[] numbers = {10, 20, 30};
numbers[0];
numbers.length;
```

Java arrays are fixed-size.

For a dynamic list, use:

```java
List<Integer> numbers = new ArrayList<>();
numbers.add(10);
numbers.get(0);
numbers.size();
```

## 5. ArrayList

Think of `ArrayList` as the common Java equivalent of a JavaScript array when you need a resizable collection.

```js
const names = [];
names.push("Pawan");
names.push("John");
names[0];
names.length;
names.includes("Pawan");
```

```java
List<String> names = new ArrayList<>();

names.add("Pawan");
names.add("John");
names.get(0);
names.size();
names.contains("Pawan");
```

Common methods:

```java
add()
get()
set()
remove()
contains()
size()
isEmpty()
clear()
```

## 6. Objects → Classes

JavaScript:

```js
class User {
    constructor(name, age) {
        this.name = name;
        this.age = age;
    }

    sayHello() {
        console.log(`Hello ${this.name}`);
    }
}

const user = new User("Pawan", 25);
```

Java:

```java
class User {
    private String name;
    private int age;

    User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    void sayHello() {
        System.out.println("Hello " + name);
    }
}

User user = new User("Pawan", 25);
```

Key difference:

```text
JavaScript → classes are relatively flexible
Java       → classes have explicit types, access control and structure
```

## 7. `this`

JavaScript:

```js
this.name = name;
```

Java:

```java
this.name = name;
```

Same basic idea:

> `this` refers to the current object.

## 8. Functions vs Methods

JavaScript:

```js
function add(a, b) {
    return a + b;
}
```

Java:

```java
int add(int a, int b) {
    return a + b;
}
```

Java methods need an explicit return type:

```java
int add(...)
String getName(...)
void print(...)
```

`void` means the method returns nothing.

## 9. Arrow Functions → Lambdas

JavaScript:

```js
const add = (a, b) => a + b;
```

Java:

```java
(a, b) -> a + b
```

Example:

```java
names.forEach(name -> System.out.println(name));
```

Java lambdas are commonly used with functional interfaces [interfaces with one abstract method].

## 10. Arrays / Collections: `map` and `filter`

JavaScript:

```js
const result = numbers
    .filter(n => n > 2)
    .map(n => n * 2);
```

Java:

```java
List<Integer> result = numbers.stream()
    .filter(n -> n > 2)
    .map(n -> n * 2)
    .toList();
```

Think:

```text
JavaScript array methods
        ↓
Java Stream operations
```

Common Java stream operations:

```java
filter()
map()
forEach()
toList()
collect()
count()
anyMatch()
allMatch()
findFirst()
```

## 11. Maps

JavaScript:

```js
const ages = new Map();

ages.set("Pawan", 25);
ages.set("John", 30);

ages.get("Pawan");
ages.has("John");
ages.delete("John");
```

Java:

```java
Map<String, Integer> ages = new HashMap<>();

ages.put("Pawan", 25);
ages.put("John", 30);

ages.get("Pawan");
ages.containsKey("John");
ages.remove("John");
```

Useful Java methods:

```java
get()
put()
remove()
containsKey()
containsValue()
getOrDefault()
size()
isEmpty()
clear()
```

## 12. Sets

JavaScript:

```js
const names = new Set();

names.add("Pawan");
names.add("Pawan");

console.log(names.has("Pawan"));
```

Java:

```java
Set<String> names = new HashSet<>();

names.add("Pawan");
names.add("Pawan");

names.contains("Pawan");
```

Both store unique values.

## 13. Equality

JavaScript:

```js
a === b
```

Java primitives:

```java
a == b
```

Java objects:

```java
a.equals(b)
```

Important:

```java
user1 == user2
```

checks whether the references point to the same object.

```java
user1.equals(user2)
```

checks logical equality, assuming the class implements `equals()` appropriately.

## 14. `null`

JavaScript:

```js
let user = null;
```

Java:

```java
User user = null;
```

Both mean there is currently no object/value.

Java:

```java
user.getName();
```

can produce:

```text
NullPointerException
```

## 15. Optional

A common Java way to explicitly represent "maybe there is a value":

```java
Optional<User> user = findUser(id);

User result = user.orElseThrow();
```

Think of it roughly as:

```js
const user = findUser(id); // could be null/undefined
```

But `Optional` is an explicit Java type, not a direct JavaScript equivalent.

## 16. Exceptions

JavaScript:

```js
try {
    doSomething();
} catch (error) {
    console.log(error);
}
```

Java:

```java
try {
    doSomething();
} catch (Exception e) {
    System.out.println(e);
}
```

Throwing:

```js
throw new Error("Something went wrong");
```

```java
throw new RuntimeException("Something went wrong");
```

Java also has:

```java
throws IOException
```

which declares that a method may throw an exception.

Remember:

```text
throw  → actually throw
throws → declare that a method may throw
```

## 17. Sorting

JavaScript:

```js
numbers.sort((a, b) => a - b);
```

Java:

```java
numbers.sort(Comparator.naturalOrder());
```

Custom ordering:

```java
numbers.sort((a, b) -> b - a);
```

For objects:

```java
users.sort(Comparator.comparing(User::getName));
```

## 18. Date / Time

JavaScript:

```js
const now = new Date();
```

Java:

```java
LocalDate today = LocalDate.now();
LocalDateTime now = LocalDateTime.now();
Instant timestamp = Instant.now();
```

Useful types:

```text
LocalDate      → date only
LocalDateTime  → date + time, no timezone
Instant        → point in time
ZonedDateTime  → date + time + timezone
```

## 19. Access Modifiers

JavaScript has relatively loose visibility rules compared with Java.

Java:

```text
private
→ only this class

package-private
→ same package

protected
→ same package + subclasses

public
→ broadly accessible
```

Example:

```java
public class User {
    private String name;

    public String getName() {
        return name;
    }
}
```

You will see these constantly in Java backend code.

## 20. Static

JavaScript:

```js
class MathUtil {
    static add(a, b) {
        return a + b;
    }
}

MathUtil.add(2, 3);
```

Java:

```java
class MathUtil {
    static int add(int a, int b) {
        return a + b;
    }
}

MathUtil.add(2, 3);
```

`static` means the member belongs to the class rather than an individual object.

## 21. Inheritance

JavaScript:

```js
class Dog extends Animal {
}
```

Java:

```java
class Dog extends Animal {
}
```

Very similar.

## 22. Interfaces

Java has a major concept without a direct JavaScript equivalent:

```java
interface PaymentMethod {
    void pay(double amount);
}
```

Implementation:

```java
class UPI implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Paid using UPI");
    }
}
```

Think:

```text
interface
→ contract describing what a class must provide
```

## 23. Abstract Classes

Java:

```java
abstract class Payment {
    abstract void pay();

    void showMessage() {
        System.out.println("Processing payment");
    }
}
```

You cannot do:

```java
new Payment();
```

A subclass must implement the abstract behavior.

Use this mental model:

```text
abstract class
→ partially implemented parent class
→ cannot be instantiated directly
```

## 24. Packages and Imports

JavaScript:

```js
import User from "./User.js";
```

Java:

```java
import com.example.User;
```

A Java package is a namespace used to organize classes:

```java
package com.example.users;
```

Common project structure:

```text
src/
├── main/
│   ├── java/
│   └── resources/
└── test/
    └── java/
```

## 25. Maven

JavaScript:

```text
package.json
npm install
npm run build
```

Java/Maven:

```text
pom.xml
./mvnw test
./mvnw package
./mvnw clean
```

`pom.xml` defines project dependencies and build configuration.

## 26. Common Java Syntax Patterns

### Create an object

```java
User user = new User("Pawan", 25);
```

### List

```java
List<User> users = new ArrayList<>();
```

### Map

```java
Map<String, Integer> ages = new HashMap<>();
```

### Loop

```java
for (User user : users) {
    System.out.println(user.getName());
}
```

### Lambda

```java
users.forEach(user -> System.out.println(user.getName()));
```

### Stream

```java
List<String> names = users.stream()
    .map(User::getName)
    .toList();
```

### Interface reference

```java
List<String> names = new ArrayList<>();
```

Read it as:

```text
variable type = List
actual object  = ArrayList
```

---

# Java Things I Must Actually Memorize

These should become automatic:

```text
Java is statically typed.

String uses methods like .length(), not .length.

ArrayList → resizable list.

List → interface.
ArrayList → implementation.

Map → key/value.
Set → unique values.

== → primitive equality / object reference comparison.
.equals() → object logical equality.

null → no object reference.

private / public → visibility.

static → belongs to the class.

new → creates an object.

extends → class inheritance.
implements → interface implementation.

throw → throw exception.
throws → declare possible exception.

filter() → keep matching elements.
map() → transform elements.

List<T> → generic collection.
```

# Java Things I Can Look Up

Don't waste brain space memorizing:

```text
Exact Stream API syntax
Comparator variations
Date formatting
Rare collection methods
Complex generic syntax
Maven plugin configuration
Rare exception classes
try-with-resources syntax
Obscure access-control rules
Unfamiliar Java library APIs
```

The goal isn't to memorize Java.

The goal is to reach the point where you see:

```java
List<User> users = repository.findAll();

return users.stream()
    .filter(User::isActive)
    .map(User::getName)
    .toList();
```

and immediately understand:

> "Get users → keep active ones → extract their names → return a list."

That's the level of Java fluency you need before moving into backend development.
