# JSON Validator in Java

This is a command-line project written in Java that validates whether a file contains **valid JSON**.
It includes a custom tokenizer and parser that supports:

- Objects (`{}`)
- Arrays (`[]`)
- Strings
- Numbers (including decimals and exponents)
- Literals (`true`,`false`,`null`)

---

## Features
- No external libraries - everything is implemented from scratch in plain Java.
- Supports nested objects and arrays.
- Detects invalid JSON structures.
- Works from command line.

---

## Project Structure

```
|---src
    |--- Main.java # Entry Point
    |--- Parser.java # JSON Parser
    |--- Token.java # Token Representation
    |--- TokenType.java # Enum for Token Types
```

---

## Usage

### 1. Compile
```bash
javac src/*.java
```

### 2. Run
```bash
java -cp src Main <path_to_json_file>
```

#### Example
```bash
java -cp src Main sample.json
```

---

## Output

If the JSON is valid:
```
Valid JSON
```

If the JSON is invalid:
```
Invalid JSON
```

--- 

## Example

sample.json
```json
{
    "name":"Alice",
    "age": 25,
    "hobbies": ["reading", "gaming"],
    "isStudent": false,
    "address": null
}
```

Run
```bash
java -cp src Main sample.json
```

Output
```text
Valid JSON
```

---

## Requirements

- Java 11+ (uses ``Files.readString``)

---

## License

This project is licensed under the MIT license