# PDDL2SPARQL

## 1. Introduction
PDDL2SPARQL is a small program which converts the text in PDDL forms to the corresponding SPARQL queries. Currently, this program aims at the relation syntax in the PDDL expressions, hence it could not converts the whole PDDL files but the relation fragments. More specifically:

### 1.1 Relationship Tuples
PDDL2SPARQL is capable of recognising relationship tuples in PDDL syntax, which can be generally divided into two scenarios:

(1). with predicate, e.g. ``` on ?Variable1 ?Variable2 ```, ``` type ?Variable Class ```

(2). without predicate, e.g. ``` holding ?Variable ```

PDDL2SPARQL is capable of differentiating the two scenarios and convert them into the corre- sponding SPARQL relationship forms.

### 1.2 Logical Symbols
PDDL2SPARQL is also capable of detecting logical symbols (i.e. ”and”, ”or”, ”not”) and converting them into corresponding syntaxes in SPARQL (”&&”, ”∥”, ”NOT EXISTS”).

### 1.3 Prefixes
When relation tuples in the given PDDL syntax contain prefix (e.g. an url), PDDL2SPARQL is capable of listing them uniquely at the beginning of the output SPARQL syntax.



## 2. How to use
Firstly add the PDDL2SPARQL.jar to your working Java project as the external JARs. Then the program can be invoked by the following codes:

```java
import java.io.IOException; 
import PDDL_SPARQL.*;

public class Test_PDDL2SPARQL {

  public static void main(String[] args) throws IOException {
  
    String InputPDDL = "Your-PDDL-Expressions";
    PDDL_SPARQL.translator(InputPDDL); 
    
  }
}
```

The console will print the converted expressions in SPARQL.

## 3. Remarks
For the time being, PDDL2SPARQL does not accept “conditional effects” (”when”), “universal/exis- tential quantifications” (”forall”/”exists”), “derived predicates”, function definitions. Which is, currently only the ”and”, ”or”, ”not” syntax (as illustrated in the Examples) are supported. This point will be addressed in the future development.
