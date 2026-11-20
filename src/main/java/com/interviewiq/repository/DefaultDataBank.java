package com.interviewiq.repository;

import com.interviewiq.model.CodingChallenge;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.Question;
import com.interviewiq.model.QuizQuestion;
import com.interviewiq.model.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Built-in comprehensive question bank, MCQ quizzes, and coding challenges.
 * Provides at least 10 high-quality technical questions per major category,
 * real MCQ questions with explanations, and standard coding assessment challenges.
 */
public final class DefaultDataBank {
    private DefaultDataBank() {}

    public static List<Question> createDefaultQuestions() {
        List<Question> q = new ArrayList<>();

        // ==========================================
        // 1. JAVA PROGRAMMING (11 Questions)
        // ==========================================
        q.add(new Question(
                "JAVA-001",
                "JVM Architecture & Memory Areas",
                "Java Programming",
                Difficulty.MEDIUM,
                Arrays.asList("JVM", "Memory", "Garbage Collection"),
                "Explain the internal architecture of the JVM and describe the different runtime data areas.",
                "The JVM consists of three main subsystems: ClassLoader Subsystem, Runtime Data Areas, and Execution Engine.\n\nRuntime Data Areas include:\n1. Method Area (Metaspace in Java 8+): Stores class-level data, static variables, runtime constant pool.\n2. Heap: Stores all objects and arrays; divided into Young Generation (Eden, S0, S1) and Old (Tenured) Generation.\n3. JVM Stacks: Thread-private; stores stack frames containing local variables, operand stack, and frame data.\n4. PC Registers: Stores address of currently executing JVM instruction per thread.\n5. Native Method Stack: Supports native code execution (C/C++).",
                "Understanding the distinction between Heap (shared, object storage) and Stack (thread-confined, execution frames) is critical for diagnosing OutOfMemoryError and StackOverflowError in production.",
                "// Stack holds reference 'p', Heap holds the Person object instance\nPerson p = new Person(\"Alice\");"
        ));

        q.add(new Question(
                "JAVA-002",
                "Difference Between String, StringBuilder, and StringBuffer",
                "Java Programming",
                Difficulty.EASY,
                Arrays.asList("Strings", "Immutability", "Concurrency"),
                "What is the difference between String, StringBuilder, and StringBuffer in Java?",
                "1. String: Immutable sequence of characters stored in the String Constant Pool (or heap). Every modification creates a new object.\n2. StringBuilder: Mutable sequence of characters introduced in Java 5. It is NOT synchronized, making it fast for single-threaded string concatenation.\n3. StringBuffer: Mutable sequence of characters, thread-safe because its public methods are synchronized, which incurs synchronization overhead.",
                "Always prefer StringBuilder for local variable concatenation in loops. For simple literals, compiler optimizes with invokedynamic (StringConcatFactory) in Java 9+.",
                "StringBuilder sb = new StringBuilder();\nfor (int i = 0; i < 100; i++) {\n    sb.append(i).append(\",\");\n}\nString result = sb.toString();"
        ));

        q.add(new Question(
                "JAVA-003",
                "HashMap Internal Working & Collision Resolution",
                "Java Programming",
                Difficulty.HARD,
                Arrays.asList("Collections", "Hashing", "Data Structures"),
                "Explain the internal implementation of HashMap in Java 8+, including hashing and treeification.",
                "HashMap is based on an array of Node buckets (Node<K,V>[] table). Key steps:\n1. Hashing: Uses hash(key) = (h = key.hashCode()) ^ (h >>> 16) to distribute high bits.\n2. Index calculation: index = (n - 1) & hash where n is power-of-two capacity.\n3. Collision Resolution: Initially uses singly linked lists. In Java 8, when a bucket has >= 8 nodes (TREEIFY_THRESHOLD) and table capacity >= 64, the bucket is transformed into a Red-Black Tree (TreeNode<K,V>), reducing search time from O(n) to O(log n).\n4. When resized or removed, if tree size shrinks to <= 6 (UNTREEIFY_THRESHOLD), it converts back to a linked list.",
                "Both equals() and hashCode() must be overridden consistently to adhere to the contract: if a.equals(b) is true, then a.hashCode() == b.hashCode().",
                "@Override\npublic int hashCode() {\n    return Objects.hash(id, name);\n}\n@Override\npublic boolean equals(Object o) {\n    if (this == o) return true;\n    if (!(o instanceof User u)) return false;\n    return id == u.id && Objects.equals(name, u.name);\n}"
        ));

        q.add(new Question(
                "JAVA-004",
                "Volatile Keyword vs Synchronized",
                "Java Programming",
                Difficulty.HARD,
                Arrays.asList("Concurrency", "Multithreading", "Memory Model"),
                "What is the difference between the volatile keyword and synchronized blocks in Java?",
                "1. volatile guarantees memory visibility and instruction reordering prevention (happens-before relationship), but DOES NOT guarantee atomicity (e.g., count++ is not atomic even if count is volatile).\n2. synchronized guarantees mutual exclusion (atomicity) as well as memory visibility, because entering a synchronized block acquires an intrinsic monitor lock and exits releases it.",
                "Use volatile for status flags where writes do not depend on the current value. Use synchronized or java.util.concurrent.atomic primitives for compound read-modify-write actions.",
                "private volatile boolean running = true;\n\npublic void stop() { running = false; }\npublic void work() {\n    while (running) { /* do work */ }\n}"
        ));

        q.add(new Question(
                "JAVA-005",
                "Java Stream API: Intermediate vs Terminal Operations",
                "Java Programming",
                Difficulty.MEDIUM,
                Arrays.asList("Streams", "Functional", "Java 8"),
                "What is the difference between intermediate and terminal operations in the Java Stream API?",
                "Intermediate operations (such as filter, map, flatMap, distinct, sorted, peek, limit) return a new Stream and are lazily executed. They do not process elements until a terminal operation is invoked.\nTerminal operations (such as collect, forEach, reduce, count, anyMatch, findFirst) traverse the stream pipeline, produce a result or side-effect, and close the stream. Once a terminal operation is called, the stream cannot be reused.",
                "Lazy evaluation enables optimizations like short-circuiting (e.g., findFirst stops traversing once first match is found) and loop fusion.",
                "List<String> names = list.stream()\n    .filter(s -> s.length() > 3)   // intermediate (lazy)\n    .map(String::toUpperCase)     // intermediate (lazy)\n    .collect(Collectors.toList()); // terminal (triggers processing)"
        ));

        q.add(new Question(
                "JAVA-006",
                "Equals() and HashCode() Contract",
                "Java Programming",
                Difficulty.MEDIUM,
                Arrays.asList("Core Java", "OOP", "Best Practices"),
                "What is the contract between equals() and hashCode() and why is it essential?",
                "The contract states:\n1. If two objects are equal according to equals(), their hashCode() must return the same integer.\n2. If two objects have the same hashCode(), they are NOT required to be equal (hash collision).\n3. If equals() is overridden, hashCode() must also be overridden.\nViolating this contract corrupts hashed collections like HashSet and HashMap, causing objects to become unretrievable.",
                "When an object is used as a HashMap key, changing any field that affects hashCode or equals after insertion makes the entry lost in the map.",
                "Set<Person> set = new HashSet<>();\nset.add(new Person(\"Bob\", 25));\n// If hashCode() is not overridden, contains returns false!\nboolean exists = set.contains(new Person(\"Bob\", 25));"
        ));

        q.add(new Question(
                "JAVA-007",
                "Java Generics and Type Erasure",
                "Java Programming",
                Difficulty.HARD,
                Arrays.asList("Generics", "JVM", "Type System"),
                "Explain Type Erasure in Java Generics and its practical implications.",
                "Java Generics were introduced in Java 5 with compile-time type safety while maintaining backward compatibility with pre-Java 5 bytecode. The Java compiler erases all type parameters:\n1. Replaces unbounded type parameters with Object and bounded type parameters with their first bound (e.g. <T extends Comparable<T>> becomes Comparable).\n2. Inserts synthetic type casts where necessary.\n3. Generates bridge methods to preserve polymorphism in extended classes.\nImplications: You cannot do `new T()`, `new T[10]`, or `instanceof List<String>` at runtime.",
                "Because of type erasure, all instances of `List<T>` share the same runtime class `List.class`.",
                "List<String> strList = new ArrayList<>();\nList<Integer> intList = new ArrayList<>();\n// Returns true because both are java.util.ArrayList.class at runtime:\nSystem.out.println(strList.getClass() == intList.getClass());"
        ));

        q.add(new Question(
                "JAVA-008",
                "Checked vs Unchecked Exceptions",
                "Java Programming",
                Difficulty.EASY,
                Arrays.asList("Exceptions", "Error Handling"),
                "What is the architectural difference between Checked and Unchecked exceptions?",
                "1. Checked Exceptions: Subclasses of Exception (excluding RuntimeException). Verified at compile-time; must be declared with `throws` or caught with `try-catch`. Used for recoverable external conditions (e.g. IOException, SQLException).\n2. Unchecked Exceptions: Subclasses of RuntimeException and Error. Not verified at compile-time. Indicate programming bugs, contract violations, or unrecoverable system failures (e.g. NullPointerException, IllegalArgumentException, OutOfMemoryError).",
                "Modern frameworks (Spring, Hibernate) wrap checked exceptions in unchecked exceptions to keep business logic decoupled from low-level infrastructure exceptions.",
                "// Unchecked exception example:\nif (input == null) throw new IllegalArgumentException(\"Input required\");"
        ));

        q.add(new Question(
                "JAVA-009",
                "CompletableFuture and Asynchronous Programming",
                "Java Programming",
                Difficulty.HARD,
                Arrays.asList("Concurrency", "Async", "Java 8"),
                "How does CompletableFuture improve over standard Future in Java concurrency?",
                "Standard Future (Java 5) only allowed blocking via `.get()` or polling via `.isDone()`. CompletableFuture (Java 8) supports non-blocking asynchronous composition:\n1. Chaining callbacks: thenApply(), thenAccept(), thenRun().\n2. Combining multiple futures: thenCombine(), allOf(), anyOf().\n3. Exception handling: exceptionally(), handle().\n4. Async thread pool control: specify custom Executor via `*Async` variants.",
                "Avoid calling .join() or .get() until absolutely necessary at the application boundary to avoid thread starvation.",
                "CompletableFuture.supplyAsync(() -> fetchUserData(userId))\n    .thenApplyAsync(user -> enrichProfile(user))\n    .thenAccept(profile -> renderUI(profile))\n    .exceptionally(ex -> { log.error(\"Error\", ex); return null; });"
        ));

        q.add(new Question(
                "JAVA-010",
                "Garbage Collection Algorithms in Modern Java",
                "Java Programming",
                Difficulty.MEDIUM,
                Arrays.asList("JVM", "Garbage Collection", "Performance"),
                "Compare the default G1 GC with ZGC in modern Java.",
                "1. G1 (Garbage-First) GC (default since Java 9): Divides heap into equal-sized regions (1MB to 32MB). Evacuates live objects from regions with the most garbage first into free regions. Uses mixed collections (Young + partially Old) to meet soft pause-time goals.\n2. ZGC (Z Garbage Collector): Ultra-low pause time GC (<1ms pauses) scalable to terabyte heaps. Performs almost all phases concurrently (marking, relocation, reference processing) using colored pointers and load barriers.",
                "G1 GC is suitable for general high-throughput workloads with predictable pauses. ZGC is best for latency-critical applications requiring minimal pause times.",
                "// VM flags to enable ZGC in modern JDK:\n// -XX:+UseZGC"
        ));

        q.add(new Question(
                "JAVA-011",
                "Java 17+ Records and Pattern Matching",
                "Java Programming",
                Difficulty.MEDIUM,
                Arrays.asList("Java 17", "Modern Java", "OOP"),
                "What are Java Records and how do they interact with Pattern Matching?",
                "Records (introduced as preview in Java 14, standard in Java 16) are transparent immutable data carriers. The compiler automatically generates private final fields, canonical constructor, accessors, equals(), hashCode(), and toString().\nCombined with Pattern Matching for `switch` and `instanceof`, records enable concise algebraic data modeling and deconstruction.",
                "Records cannot extend another class because they implicitly extend java.lang.Record, but they can implement interfaces.",
                "public record UserProfile(String id, String username, int score) {}\n\n// Pattern matching instanceof:\nif (obj instanceof UserProfile(var id, var name, var score)) {\n    System.out.println(name + \" has score \" + score);\n}"
        ));

        // ==========================================
        // 2. PYTHON PROGRAMMING (10 Questions)
        // ==========================================
        q.add(new Question(
                "PY-001",
                "Global Interpreter Lock (GIL) in CPython",
                "Python Programming",
                Difficulty.HARD,
                Arrays.asList("GIL", "Concurrency", "CPython"),
                "What is the GIL in Python, why does it exist, and how do you bypass it?",
                "The Global Interpreter Lock (GIL) is a mutual exclusion lock used by CPython to prevent multiple native OS threads from executing Python bytecode simultaneously. It exists because CPython's memory management is not thread-safe (it uses reference counting).\nImpact: Multi-threaded Python programs do not achieve true CPU-bound parallelism.\nWorkarounds: 1) Use the `multiprocessing` module for separate process address spaces. 2) Offload heavy computations to C-extensions like NumPy or Cython. 3) In Python 3.13+, free-threaded mode (PEP 703) provides experimental GIL-free execution.",
                "For I/O-bound tasks (network, disk), Python threads release the GIL during syscalls, making threading or asyncio highly effective.",
                "from multiprocessing import Pool\n\nwith Pool(processes=4) as pool:\n    results = pool.map(cpu_intensive_task, data_chunks)"
        ));

        q.add(new Question(
                "PY-002",
                "Python Memory Management and Garbage Collection",
                "Python Programming",
                Difficulty.MEDIUM,
                Arrays.asList("Memory", "Garbage Collection", "Internals"),
                "How does Python manage memory and handle circular references?",
                "Python uses two primary mechanisms for memory management:\n1. Reference Counting: Every object has an `ob_refcnt`. When references drop to 0, memory is immediately deallocated.\n2. Cyclic Garbage Collector: Since reference counting cannot detect circular references (e.g. A references B and B references A), Python includes a generational GC (`gc` module) that tracks container objects across 3 generations (Gen 0, Gen 1, Gen 2) using pointer traversal.",
                "Python also uses PyMalloc, a specialized small-object allocator (<= 512 bytes) using arenas, pools, and blocks to reduce OS malloc overhead.",
                "import gc\nprint(gc.get_count())  # (gen0, gen1, gen2 counts)\ngc.collect()           # manual collection"
        ));

        q.add(new Question(
                "PY-003",
                "Generators vs Iterators in Python",
                "Python Programming",
                Difficulty.EASY,
                Arrays.asList("Generators", "Iterators", "Memory"),
                "Explain the difference between an iterator and a generator in Python.",
                "An iterator is an object that implements the iterator protocol: `__iter__()` returning itself and `__next__()` returning elements until raising `StopIteration`.\nA generator is a simpler way to create iterators using functions with the `yield` statement or generator expressions `(x for x in data)`. Generators automatically maintain their local state between yields and lazy-evaluate values on demand, using O(1) memory.",
                "Generators are ideal for streaming large datasets, file processing, and infinite pipelines.",
                "def fibonacci():\n    a, b = 0, 1\n    while True:\n        yield a\n        a, b = b, a + b"
        ));

        q.add(new Question(
                "PY-004",
                "Python Decorators and @functools.wraps",
                "Python Programming",
                Difficulty.MEDIUM,
                Arrays.asList("Decorators", "Functional", "Metaprogramming"),
                "How do decorators work in Python and why is @functools.wraps important?",
                "A decorator is a callable that takes a function as input, extends or alters its behavior without modifying its source code, and returns a callable.\n`@functools.wraps` is crucial because wrapping a function replaces its `__name__`, `__doc__`, and module metadata with that of the wrapper. Using `@functools.wraps(fn)` preserves the original function's introspection attributes, aiding debugging and documentation tools.",
                "Decorators can also accept arguments by adding an extra outer wrapper layer returning the decorator.",
                "import functools\n\ndef log_call(func):\n    @functools.wraps(func)\n    def wrapper(*args, **kwargs):\n        print(f\"Calling {func.__name__}\")\n        return func(*args, **kwargs)\n    return wrapper"
        ));

        q.add(new Question(
                "PY-005",
                "Mutable Default Arguments Pitfall",
                "Python Programming",
                Difficulty.EASY,
                Arrays.asList("Gotchas", "Functions", "Best Practices"),
                "Why should you never use mutable default arguments like `def append_to(item, target=[])` in Python?",
                "Default argument expressions in Python are evaluated once at function definition time (when the module is loaded), NOT each time the function is called. If the default value is mutable (like a list or dict), all subsequent calls that omit the argument share the identical mutated object.",
                "The idiomatic pattern is to use `None` as the default argument and initialize the mutable object inside the function body.",
                "# Bad:\ndef append_item(x, lst=[]):\n    lst.append(x)\n    return lst\n\n# Correct:\ndef append_item(x, lst=None):\n    if lst is None:\n        lst = []\n    lst.append(x)\n    return lst"
        ));

        q.add(new Question(
                "PY-006",
                "Dunder / Magic Methods and Context Managers",
                "Python Programming",
                Difficulty.MEDIUM,
                Arrays.asList("OOP", "Context Managers", "Dunder"),
                "How do context managers work in Python and which dunder methods implement them?",
                "Context managers manage resource setup and teardown cleanly using the `with` statement. They are implemented via two dunder methods:\n1. `__enter__(self)`: Prepares the resource and returns an object assigned to `as target`.\n2. `__exit__(self, exc_type, exc_val, exc_tb)`: Always executed upon exiting the block, even if an exception occurs. If it returns `True`, the exception is swallowed; otherwise, the exception propagates.",
                "Alternatively, the `contextlib.contextmanager` decorator can turn a generator function into a context manager.",
                "class DatabaseConnection:\n    def __enter__(self):\n        self.conn = connect()\n        return self.conn\n    def __exit__(self, exc_type, exc_val, exc_tb):\n        self.conn.close()\n        return False"
        ));

        q.add(new Question(
                "PY-007",
                "List Comprehension vs Generator Expressions",
                "Python Programming",
                Difficulty.EASY,
                Arrays.asList("Comprehensions", "Performance"),
                "When should you choose a Generator Expression over a List Comprehension?",
                "List Comprehensions `[x for x in seq]` allocate and construct the entire list in memory immediately. They have O(N) space complexity.\nGenerator Expressions `(x for x in seq)` evaluate items one at a time on demand. They have O(1) space complexity.\nUse generator expressions when piping data into functions that reduce streams (e.g., `sum()`, `max()`, `any()`) or when dealing with massive datasets where loading everything into memory is wasteful or causes MemoryError.",
                "Use list comprehension only when you need indexing, slicing, multiple passes, or require a concrete list.",
                "total = sum(x * x for x in range(1_000_000))  # O(1) memory!"
        ));

        q.add(new Question(
                "PY-008",
                "Method Resolution Order (MRO) and C3 Linearization",
                "Python Programming",
                Difficulty.HARD,
                Arrays.asList("OOP", "Inheritance", "MRO"),
                "What is Method Resolution Order (MRO) in Python and how does C3 Linearization work?",
                "MRO defines the order in which Python searches for attributes and methods in a class hierarchy with multiple inheritance. Python uses the C3 Linearization algorithm to guarantee:\n1. Children precede parents.\n2. Order of parents in the inheritance list is preserved (local precedence order).\n3. Monotonicity: if class C inherits from B, the precedence of ancestors in B must not be reversed in C.",
                "You can inspect any class's MRO using `Class.__mro__` or `Class.mro()`.",
                "class A: pass\nclass B(A): pass\nclass C(A): pass\nclass D(B, C): pass\n# MRO for D: [D, B, C, A, object]\nprint(D.__mro__)"
        ));

        q.add(new Question(
                "PY-009",
                "Asyncio Event Loop and Coroutines",
                "Python Programming",
                Difficulty.HARD,
                Arrays.asList("Asyncio", "Concurrency", "Coroutines"),
                "How does Python's asyncio single-threaded concurrency model work?",
                "Python asyncio uses cooperative multitasking managed by an Event Loop running on a single thread. Coroutines are defined with `async def` and pause their execution at `await` expressions without blocking the OS thread. The event loop switches execution to other ready tasks while waiting for I/O sockets or timers, achieving high concurrency with low overhead.",
                "Never run CPU-blocking code inside an asyncio event loop; offload CPU tasks to an executor using `loop.run_in_executor()`.",
                "import asyncio\n\nasync def fetch_api(endpoint):\n    await asyncio.sleep(1) # simulates network I/O\n    return {\"data\": endpoint}\n\nasync def main():\n    results = await asyncio.gather(fetch_api(\"/users\"), fetch_api(\"/orders\"))"
        ));

        q.add(new Question(
                "PY-010",
                "Shallow Copy vs Deep Copy in Python",
                "Python Programming",
                Difficulty.EASY,
                Arrays.asList("Copy", "Memory", "Basics"),
                "Explain the difference between a shallow copy and a deep copy in Python.",
                "1. Shallow Copy (`copy.copy()`): Creates a new outer compound object, but inserts references to the child objects contained in the original. Mutating nested objects affects both copies.\n2. Deep Copy (`copy.deepcopy()`): Recursively duplicates both the compound object and all nested objects it references, creating a completely independent clone.",
                "For flat primitive collections (like a list of integers), a shallow copy behaves identically to an independent copy.",
                "import copy\norig = [[1, 2], [3, 4]]\nshallow = copy.copy(orig)\ndeep = copy.deepcopy(orig)\n\norig[0][0] = 99\n# shallow[0][0] is now 99!\n# deep[0][0] is still 1!"
        ));

        // ==========================================
        // 3. DATA STRUCTURES & ALGORITHMS (11 Questions)
        // ==========================================
        q.add(new Question(
                "DSA-001",
                "Time Complexity of Hash Tables vs Binary Search Trees",
                "Data Structures & Algorithms",
                Difficulty.MEDIUM,
                Arrays.asList("Complexity", "Hash Table", "BST"),
                "Compare the time complexity of Hash Tables and Balanced BSTs (AVL, Red-Black) for Search, Insert, and Delete.",
                "1. Hash Table:\n- Average: O(1) for Search, Insert, and Delete.\n- Worst Case: O(N) when all keys hash to the same bucket (or O(log N) in Java 8 treeified buckets).\n- Disadvantage: Unordered, does not support range queries or finding predecessor/successor efficiently.\n2. Balanced BST (AVL / Red-Black Tree):\n- Average and Worst Case: O(log N) for Search, Insert, and Delete.\n- Advantage: Elements remain strictly sorted; supports O(log N) predecessor, successor, floor, ceiling, and O(k + log N) range searches.",
                "Use Hash Tables when fast key-value lookups are primary. Use Balanced BSTs when sorted order or range queries are required.",
                "// TreeMap is Red-Black Tree (O(log n)), HashMap is Hash Table (O(1) average)"
        ));

        q.add(new Question(
                "DSA-002",
                "Two Pointers vs Sliding Window Pattern",
                "Data Structures & Algorithms",
                Difficulty.MEDIUM,
                Arrays.asList("Patterns", "Two Pointers", "Sliding Window"),
                "What is the difference between Two Pointers and Sliding Window algorithmic patterns?",
                "1. Two Pointers: Typically used on sorted arrays or linked lists where two pointers move towards each other (left from 0, right from n-1) or at different speeds (fast and slow pointer for cycle detection). Reduces O(N^2) search space to O(N).\n2. Sliding Window: Maintains a dynamic or fixed subarray/substring boundary [left, right] that expands to include elements and contracts to satisfy constraints. Tracks running metrics (sum, frequency map) in O(N) total time.",
                "Both patterns avoid nested loops by guaranteeing each element is processed at most twice (entered and exited once).",
                "// Sliding window dynamic template:\nint left = 0;\nfor (int right = 0; right < n; right++) {\n    add(arr[right]);\n    while (invalidCondition()) {\n        remove(arr[left++]);\n    }\n    maxLen = Math.max(maxLen, right - left + 1);\n}"
        ));

        q.add(new Question(
                "DSA-003",
                "Detecting Cycles in a Directed vs Undirected Graph",
                "Data Structures & Algorithms",
                Difficulty.HARD,
                Arrays.asList("Graphs", "DFS", "Cycles"),
                "How do cycle detection algorithms differ between directed and undirected graphs?",
                "1. Undirected Graph:\n- DFS: A cycle exists if we visit an already visited node that is NOT the immediate parent of the current node.\n- Union-Find (Disjoint Set Union): If two vertices of an edge are already in the same connected component, a cycle exists.\n2. Directed Graph:\n- DFS with 3 states (White/Unvisited, Gray/Visiting/Current Stack, Black/Visited): A cycle exists if and only if DFS encounters a Gray node (a back-edge pointing to an ancestor in the active recursion call stack).\n- Kahn's Algorithm (Topological Sort): If topological sorting cannot process all V vertices, a cycle exists.",
                "Never use simple 2-color visited array on a directed graph; cross-edges to already finished nodes do not form cycles.",
                "// 3-state DFS for directed graph:\nboolean hasCycle(int u, int[] state) {\n    state[u] = 1; // Visiting (Gray)\n    for (int v : adj[u]) {\n        if (state[v] == 1) return true; // Back-edge detected!\n        if (state[v] == 0 && hasCycle(v, state)) return true;\n    }\n    state[u] = 2; // Visited (Black)\n    return false;\n}"
        ));

        q.add(new Question(
                "DSA-004",
                "Kadane's Algorithm for Maximum Subarray Sum",
                "Data Structures & Algorithms",
                Difficulty.EASY,
                Arrays.asList("Dynamic Programming", "Arrays", "Kadane"),
                "Explain Kadane's Algorithm and analyze its time and space complexity.",
                "Kadane's algorithm solves the Maximum Subarray problem in O(N) time and O(1) space. At each index `i`, we make a local decision:\nEither extend the previous subarray: `currentSum + arr[i]`\nOr start a new subarray at `arr[i]`: `arr[i]`\nThus: `currentSum = Math.max(arr[i], currentSum + arr[i])`\nWe keep track of `maxSum = Math.max(maxSum, currentSum)` throughout the iteration.",
                "Handle the case where all numbers are negative by initializing maxSum to arr[0] or Integer.MIN_VALUE instead of 0.",
                "public int maxSubArray(int[] nums) {\n    int currentSum = nums[0];\n    int maxSum = nums[0];\n    for (int i = 1; i < nums.length; i++) {\n        currentSum = Math.max(nums[i], currentSum + nums[i]);\n        maxSum = Math.max(maxSum, currentSum);\n    }\n    return maxSum;\n}"
        ));

        q.add(new Question(
                "DSA-005",
                "Dijkstra's vs Bellman-Ford Shortest Path",
                "Data Structures & Algorithms",
                Difficulty.HARD,
                Arrays.asList("Graphs", "Shortest Path", "Algorithms"),
                "When should you choose Bellman-Ford over Dijkstra's algorithm for shortest path finding?",
                "1. Dijkstra's Algorithm: Uses a Greedy strategy with a Min-Heap. Time complexity: O((V + E) log V). CANNOT handle edges with negative weights because once a vertex is finalized, its shortest distance is assumed locked.\n2. Bellman-Ford Algorithm: Uses Dynamic Programming. Relaxes all E edges V-1 times. Time complexity: O(V * E). CAN handle negative edge weights and can detect negative weight cycles (if an edge can still be relaxed in the V-th iteration).",
                "For general non-negative graphs, Dijkstra is vastly superior in speed. For graphs with negative weights or currency arbitrage detection, Bellman-Ford or SPFA is required.",
                "// Bellman-Ford relaxation step:\nfor (int i = 1; i < V; i++) {\n    for (Edge e : edges) {\n        if (dist[e.u] != INF && dist[e.u] + e.weight < dist[e.v]) {\n            dist[e.v] = dist[e.u] + e.weight;\n        }\n    }\n}"
        ));

        q.add(new Question(
                "DSA-006",
                "LRU Cache Design & Complexity",
                "Data Structures & Algorithms",
                Difficulty.HARD,
                Arrays.asList("Design", "Hash Map", "Doubly Linked List"),
                "How do you design an LRU (Least Recently Used) Cache with O(1) get and put operations?",
                "An optimal LRU Cache combines two data structures:\n1. Doubly Linked List: Maintains access order. The most recently used element is moved to the Head; the least recently used element resides at the Tail. Node removal and insertion are O(1).\n2. Hash Map: Maps Key -> DoublyLinkedListNode. Provides O(1) lookup to find the node's memory pointer directly in the list without traversing.",
                "Using pseudo Head and pseudo Tail dummy nodes simplifies boundary code by eliminating null checks during insertions and deletions.",
                "class LRUCache {\n    private Map<Integer, Node> map = new HashMap<>();\n    private Node head, tail;\n    private int capacity;\n    // get(key): lookup node in map, move to head, return val\n    // put(key, val): update or insert node at head; if size > cap, evict tail.prev\n}"
        ));

        q.add(new Question(
                "DSA-007",
                "QuickSort vs MergeSort",
                "Data Structures & Algorithms",
                Difficulty.MEDIUM,
                Arrays.asList("Sorting", "Divide and Conquer"),
                "Compare QuickSort and MergeSort across time, space, and stability.",
                "1. QuickSort:\n- Average Time: O(N log N), Worst Time: O(N^2) with poor pivot choice.\n- Space: O(log N) auxiliary stack space for recursion (in-place partitioning).\n- Stability: Unstable in standard form.\n- Better for: Primitive arrays, CPU cache locality.\n2. MergeSort:\n- Time: O(N log N) in all cases (Best, Average, Worst).\n- Space: O(N) auxiliary array space.\n- Stability: Stable.\n- Better for: Linked lists, objects where stability is needed, external sorting.",
                "Java's `Arrays.sort(primitive[])` uses Dual-Pivot QuickSort, while `Arrays.sort(Object[])` uses TimSort (hybrid MergeSort/InsertionSort).",
                "// Merge sort guarantees O(N log N) worst-case, Quick sort gives better cache performance"
        ));

        q.add(new Question(
                "DSA-008",
                "Dynamic Programming: Memoization vs Tabulation",
                "Data Structures & Algorithms",
                Difficulty.MEDIUM,
                Arrays.asList("Dynamic Programming", "Recursion", "Optimization"),
                "What is the difference between Top-Down Memoization and Bottom-Up Tabulation in DP?",
                "1. Top-Down (Memoization): Solves the problem recursively from original state down to base cases, caching computed subproblem results in a hash map or table. Only computes subproblems that are actually needed, but incurs call stack overhead and risk of StackOverflowError for deep recursion.\n2. Bottom-Up (Tabulation): Solves the smallest subproblems first and iteratively fills a table up to the target state. Eliminates recursion overhead and often allows Space Optimization (e.g. keeping only the last 2 states instead of full N-length array).",
                "Space optimization in bottom-up DP often reduces space complexity from O(N) to O(1) or O(N*M) to O(min(N, M)).",
                "// Tabulation space-optimized Fibonacci:\nint a = 0, b = 1;\nfor (int i = 2; i <= n; i++) {\n    int c = a + b;\n    a = b;\n    b = c;\n}"
        ));

        q.add(new Question(
                "DSA-009",
                "Trie (Prefix Tree) Applications and Complexity",
                "Data Structures & Algorithms",
                Difficulty.MEDIUM,
                Arrays.asList("Trie", "Trees", "String"),
                "What is a Trie and what advantages does it offer over a Hash Table for string operations?",
                "A Trie is a tree data structure where each node represents a character of a string. Edges connect characters that form prefixes of stored keys.\nAdvantages over Hash Table:\n1. Autocomplete / Prefix Search: Finding all words matching a prefix takes O(L) time where L is prefix length, whereas Hash Table requires scanning all keys O(N * L).\n2. No Hash Collisions: Deterministic path lookup.\n3. Lexicographical Ordering: Traversing the trie in-order naturally yields alphabetically sorted words.",
                "Space consumption can be high for sparse alphabets, which can be mitigated with Compressed Tries (Radix Trees).",
                "class TrieNode {\n    TrieNode[] children = new TrieNode[26];\n    boolean isEndOfWord;\n}"
        ));

        q.add(new Question(
                "DSA-010",
                "Floyd's Cycle Detection Algorithm (Tortoise and Hare)",
                "Data Structures & Algorithms",
                Difficulty.EASY,
                Arrays.asList("Linked List", "Pointers"),
                "How does Floyd's Tortoise and Hare algorithm detect a cycle in a Linked List in O(1) space?",
                "Use two pointers initialized at the head: slow advances by 1 node, fast advances by 2 nodes per step.\n1. If fast or fast.next reaches null, no cycle exists.\n2. If fast == slow at any point, a cycle exists because the fast pointer laps the slow pointer within the loop.\nTo find the starting node of the cycle: reset slow to head while keeping fast at the meeting point. Advance both 1 step at a time; they will meet exactly at the cycle's entry point.",
                "Mathematical proof: Distance from head to cycle start equals distance from meeting point to cycle start along the loop.",
                "ListNode slow = head, fast = head;\nwhile (fast != null && fast.next != null) {\n    slow = slow.next;\n    fast = fast.next.next;\n    if (slow == fast) return true; // Cycle detected!\n}\nreturn false;"
        ));

        q.add(new Question(
                "DSA-011",
                "Binary Search: Handling Off-By-One Errors & Overflow",
                "Data Structures & Algorithms",
                Difficulty.EASY,
                Arrays.asList("Binary Search", "Arrays"),
                "How do you prevent integer overflow and off-by-one errors in Binary Search?",
                "1. Integer Overflow: Writing `mid = (low + high) / 2` causes integer overflow when `low + high > Integer.MAX_VALUE`. Safe calculation: `mid = low + (high - low) / 2` or `(low + high) >>> 1`.\n2. Off-by-one errors: Define strict loop invariants:\n- If search interval is closed `[low, high]`, use `while (low <= high)` and update `low = mid + 1` or `high = mid - 1`.\n- If search interval is half-open `[low, high)`, use `while (low < high)` and update `high = mid`.",
                "Binary search is not limited to arrays; it applies to any monotonic decision predicate f(x) -> {false..., true...}.",
                "int low = 0, high = arr.length - 1;\nwhile (low <= high) {\n    int mid = low + (high - low) / 2;\n    if (arr[mid] == target) return mid;\n    else if (arr[mid] < target) low = mid + 1;\n    else high = mid - 1;\n}\nreturn -1;"
        ));

        // ==========================================
        // 4. OBJECT-ORIENTED PROGRAMMING (10 Questions)
        // ==========================================
        q.add(new Question(
                "OOP-001",
                "SOLID Principles Breakdown",
                "Object-Oriented Programming",
                Difficulty.MEDIUM,
                Arrays.asList("SOLID", "Design Principles", "Architecture"),
                "Name and explain all five SOLID principles of Object-Oriented Design.",
                "1. Single Responsibility Principle (SRP): A class should have one, and only one, reason to change.\n2. Open/Closed Principle (OCP): Software entities should be open for extension, but closed for modification.\n3. Liskov Substitution Principle (LSP): Subtypes must be substitutable for their base types without altering program correctness.\n4. Interface Segregation Principle (ISP): Clients should not be forced to depend on interfaces they do not use (prefer small, role-specific interfaces).\n5. Dependency Inversion Principle (DIP): High-level modules should not depend on low-level modules; both should depend on abstractions.",
                "Violating LSP often manifests when subclasses throw UnsupportedOperationException for inherited methods (e.g. Square extending Rectangle).",
                "// DIP Example: Service depends on PaymentGateway interface, not PayPalGateway class\npublic class OrderService {\n    private final PaymentGateway gateway;\n    public OrderService(PaymentGateway gateway) { this.gateway = gateway; }\n}"
        ));

        q.add(new Question(
                "OOP-002",
                "Composition Over Inheritance",
                "Object-Oriented Programming",
                Difficulty.EASY,
                Arrays.asList("Design Principles", "Inheritance", "Composition"),
                "Why is 'Composition over Inheritance' widely recommended in modern software engineering?",
                "Inheritance introduces tight coupling: changes in the superclass can inadvertently break subclass invariants (the fragile base class problem). Inheritance exposes subclass to superclass implementation details (breaks encapsulation) and is fixed at compile-time.\nComposition models a 'has-a' relationship rather than 'is-a'. It allows dynamic runtime behavior swapping, easier mocking for unit testing, and promotes high cohesion and loose coupling.",
                "Inheritance should only be used when true behavioral subtyping exists (LSP holds) and polymorphic substitution is necessary.",
                "// Composition:\nclass Car {\n    private Engine engine;\n    public void start() { engine.ignite(); }\n}"
        ));

        q.add(new Question(
                "OOP-003",
                "Factory Method vs Abstract Factory Pattern",
                "Object-Oriented Programming",
                Difficulty.MEDIUM,
                Arrays.asList("Design Patterns", "Creational"),
                "Distinguish between Factory Method and Abstract Factory design patterns.",
                "1. Factory Method: Defines an interface for creating a single object, but lets subclasses decide which class to instantiate (uses inheritance to defer instantiation to subclasses).\n2. Abstract Factory: Provides an interface for creating families of related or dependent objects without specifying their concrete classes (uses composition; a factory object containing multiple factory methods).",
                "Example: GUI factory producing matching buttons and scrollbars for Windows vs MacOS is an Abstract Factory.",
                "// Abstract Factory:\ninterface GUIFactory {\n    Button createButton();\n    ScrollBar createScrollBar();\n}"
        ));

        q.add(new Question(
                "OOP-004",
                "Singleton Pattern: Thread-Safe Implementation & Pitfalls",
                "Object-Oriented Programming",
                Difficulty.MEDIUM,
                Arrays.asList("Design Patterns", "Singleton", "Concurrency"),
                "How do you implement a robust thread-safe Singleton in Java and what are its potential pitfalls?",
                "The two most recommended approaches:\n1. Bill Pugh Singleton (Initialization-on-demand holder): Relies on JVM class loading guarantees; lazy, thread-safe, and zero synchronization overhead.\n2. Enum Singleton (Joshua Bloch): Single-element enum. Immune to reflection attacks, serialization tampering, and multi-threading issues.\nDouble-checked locking requires the instance variable to be declared `volatile` to prevent CPU instruction reordering.",
                "Singletons can act as hidden dependencies and introduce global state, complicating unit test isolation.",
                "public class Singleton {\n    private Singleton() {}\n    private static class Holder {\n        private static final Singleton INSTANCE = new Singleton();\n    }\n    public static Singleton getInstance() { return Holder.INSTANCE; }\n}"
        ));

        q.add(new Question(
                "OOP-005",
                "Polymorphism: Compile-Time vs Runtime",
                "Object-Oriented Programming",
                Difficulty.EASY,
                Arrays.asList("Polymorphism", "Core OOP"),
                "What is the difference between Compile-Time and Runtime Polymorphism?",
                "1. Compile-Time (Static) Polymorphism: Achieved via Method Overloading (methods with same name but different parameter count or types) and Operator Overloading (in languages like C++). Method call resolution occurs at compile-time via method signatures.\n2. Runtime (Dynamic) Polymorphism: Achieved via Method Overriding and Interfaces. Resolution occurs at runtime using dynamic dispatch (vtable / virtual method table) based on the actual object instance type on the heap.",
                "Private, static, and final methods cannot be overridden; hence they use static binding instead of dynamic dispatch.",
                "Animal a = new Dog();\na.makeSound(); // Invokes Dog's makeSound() at runtime via virtual method invocation"
        ));

        q.add(new Question(
                "OOP-006",
                "Observer Pattern & Real-World Use Cases",
                "Object-Oriented Programming",
                Difficulty.MEDIUM,
                Arrays.asList("Design Patterns", "Behavioral"),
                "Explain the Observer pattern and identify where it is used in real software architectures.",
                "The Observer pattern defines a one-to-many dependency between objects: when the subject (publisher) changes state, all its registered observers (subscribers) are notified and updated automatically.\nReal-world applications:\n- GUI event listeners (Swing ActionListeners, Android onClickListeners)\n- Reactive programming (RxJava, Project Reactor, EventEmitter)\n- Message queues and pub-sub architectures (Kafka, RabbitMQ, SNS).",
                "Ensure observers can unregister to prevent memory leaks (known as the 'lapsed listener problem').",
                "interface Observer { void update(String event); }\nclass EventPublisher {\n    private List<Observer> observers = new ArrayList<>();\n    public void notifyAll(String event) {\n        observers.forEach(o -> o.update(event));\n    }\n}"
        ));

        q.add(new Question(
                "OOP-007",
                "Encapsulation vs Abstraction",
                "Object-Oriented Programming",
                Difficulty.EASY,
                Arrays.asList("Core OOP", "Principles"),
                "What is the key difference between Abstraction and Encapsulation?",
                "1. Encapsulation: The bundling of data (fields) and methods that operate on that data into a single unit (class), while restricting direct external access to internal state using access modifiers (private, protected). It solves the problem of data hiding and integrity.\n2. Abstraction: Hiding internal implementation complexity and showing only the essential feature interface to the outside world (e.g. interfaces, abstract classes). It solves problems at the design level ('what' an object does rather than 'how').",
                "Encapsulation is the implementation technique used to enforce abstraction boundaries.",
                "// Abstraction: List interface defines operations\nList<String> list = new ArrayList<>();\n// Encapsulation: ArrayList internal Object[] elementData is private"
        ));

        q.add(new Question(
                "OOP-008",
                "Strategy Pattern for Eliminating Conditionals",
                "Object-Oriented Programming",
                Difficulty.MEDIUM,
                Arrays.asList("Design Patterns", "Refactoring"),
                "How does the Strategy Pattern eliminate sprawling switch/if-else chains?",
                "The Strategy Pattern defines a family of algorithms, encapsulates each one into a separate class conforming to a common interface, and makes them interchangeable at runtime.\nInstead of a massive switch statement checking payment types (`if (type.equals(\"CREDIT\")) ... else if (...)`), the client context accepts a `PaymentStrategy` object and simply delegates `strategy.pay(amount)`.",
                "This satisfies the Open/Closed Principle: adding a new payment method requires adding a new class without modifying existing code.",
                "interface PaymentStrategy { void pay(int amount); }\nclass PayPalStrategy implements PaymentStrategy { public void pay(int a) { /* pay */ } }\nclass Cart {\n    public void checkout(PaymentStrategy strategy) { strategy.pay(total); }\n}"
        ));

        q.add(new Question(
                "OOP-009",
                "Coupling vs Cohesion",
                "Object-Oriented Programming",
                Difficulty.EASY,
                Arrays.asList("Software Design", "Metrics"),
                "Explain the concepts of High Cohesion and Low Coupling.",
                "1. High Cohesion: Measures how focused and closely related the responsibilities inside a single module/class are. A class with high cohesion does one job well (aligns with SRP), making it easy to understand, test, and maintain.\n2. Low Coupling: Measures how independent modules are from one another. Low coupling means changes to one module do not ripple into others. Achieved through interfaces, dependency injection, and message passing.",
                "The ultimate goal of clean architecture is high cohesion within modules and loose coupling between modules.",
                "// Low coupling: Class interacts with Interface, not concrete implementation"
        ));

        q.add(new Question(
                "OOP-010",
                "Decorator vs Adapter Pattern",
                "Object-Oriented Programming",
                Difficulty.MEDIUM,
                Arrays.asList("Design Patterns", "Structural"),
                "Compare the Decorator pattern and the Adapter pattern.",
                "1. Adapter Pattern: Converts the existing interface of an incompatible class into another interface expected by clients. It changes 'how' the interface looks without adding new behavior (e.g. adapting legacy 3rd-party logger to SLF4J).\n2. Decorator Pattern: Wraps an object to attach additional responsibilities dynamically at runtime while preserving the identical interface. It enhances behavior without subclass explosion (e.g. Java I/O streams: `new BufferedReader(new FileReader(file))`).",
                "Adapter makes things work after they're designed; Decorator enhances classes without changing their interface.",
                "// Decorator adds buffering to FileReader while still being a Reader\nReader r = new BufferedReader(new FileReader(\"data.txt\"));"
        ));

        // ==========================================
        // 5. DATABASE MANAGEMENT SYSTEMS (DBMS) (10 Questions)
        // ==========================================
        q.add(new Question(
                "DBMS-001",
                "ACID Properties in Relational Databases",
                "Database Management Systems",
                Difficulty.MEDIUM,
                Arrays.asList("ACID", "Transactions", "Reliability"),
                "Define each of the ACID properties and explain how DBMS engines enforce them.",
                "1. Atomicity: 'All or nothing'. The transaction completes entirely or is rolled back. Enforced via Write-Ahead Logging (WAL) / Undo logs.\n2. Consistency: Ensures database transitions from one valid state to another, satisfying all constraints, schemas, and triggers.\n3. Isolation: Concurrent transactions execute without interfering with one another. Enforced via locking mechanisms and Multiversion Concurrency Control (MVCC).\n4. Durability: Once a transaction commits, its changes survive power loss or system crashes. Enforced via Redo logs and fsync flushing to disk.",
                "ACID guarantees make relational databases ideal for financial systems and transactional ledgers.",
                "BEGIN TRANSACTION;\nUPDATE accounts SET balance = balance - 100 WHERE id = 1;\nUPDATE accounts SET balance = balance + 100 WHERE id = 2;\nCOMMIT;"
        ));

        q.add(new Question(
                "DBMS-002",
                "Transaction Isolation Levels & Concurrency Phenomena",
                "Database Management Systems",
                Difficulty.HARD,
                Arrays.asList("Transactions", "Isolation", "Concurrency"),
                "List the 4 SQL Transaction Isolation Levels and the anomalies each prevents.",
                "From lowest to highest isolation:\n1. Read Uncommitted: Prevents nothing. Susceptible to Dirty Reads (reading uncommitted changes of another transaction), Non-repeatable Reads, and Phantom Reads.\n2. Read Committed (Postgres default): Prevents Dirty Reads. Still permits Non-repeatable Reads (re-reading a row returns different data) and Phantom Reads (re-querying returns newly inserted rows).\n3. Repeatable Read (MySQL InnoDB default): Prevents Dirty Reads and Non-repeatable Reads. In standard SQL susceptible to Phantom Reads (InnoDB prevents phantoms using Next-Key Locks).\n4. Serializable: Full isolation. Executes transactions as if sequentially. Prevents all anomalies but incurs the highest locking overhead.",
                "Higher isolation levels reduce throughput due to lock contention or optimistic concurrency retry aborts.",
                "SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;"
        ));

        q.add(new Question(
                "DBMS-003",
                "B-Tree vs B+ Tree Indexing",
                "Database Management Systems",
                Difficulty.MEDIUM,
                Arrays.asList("Indexing", "B+ Tree", "Performance"),
                "Why do relational databases use B+ Trees instead of standard B-Trees for disk-based indexing?",
                "1. Leaf-only data storage: In a B+ Tree, internal nodes store only routing keys, allowing many more keys per index page (higher fan-out). This reduces tree height and minimizes disk I/O.\n2. Sequential Leaf Chaining: Leaf nodes are linked as a doubly-linked list, enabling efficient O(k) range scans (`WHERE age BETWEEN 20 AND 30`) by locating the start key and scanning leaf pointers sequentially.\n3. Predictable search latency: In a B+ Tree, every query traverses to a leaf node, guaranteeing consistent search depth.",
                "Clustered index stores actual row data in the primary B+ tree leaves; secondary indexes store primary keys pointing to data.",
                "CREATE INDEX idx_user_email ON users(email);"
        ));

        q.add(new Question(
                "DBMS-004",
                "Database Normalization (1NF through BCNF)",
                "Database Management Systems",
                Difficulty.MEDIUM,
                Arrays.asList("Normalization", "Schema Design"),
                "Explain the progression of database normalization from 1NF to BCNF.",
                "1. 1NF: Atomic column values (no repeating groups or arrays) and a unique primary key.\n2. 2NF: Meets 1NF + no Partial Functional Dependency (all non-key attributes are fully dependent on the entire primary key, not part of a composite key).\n3. 3NF: Meets 2NF + no Transitive Dependency (non-key attributes must depend only on candidate keys, not on other non-key attributes: 'X -> Y and Y -> Z').\n4. BCNF (Boyce-Codd NF): Strict 3NF where for every functional dependency X -> Y, X must be a super key.",
                "Denormalization is deliberately applied in read-heavy analytics/warehousing (star schemas) to reduce expensive joins.",
                "-- Normalized to 3NF:\n-- Orders(order_id, customer_id, order_date)\n-- Customers(customer_id, customer_name, customer_city)"
        ));

        q.add(new Question(
                "DBMS-005",
                "SQL Joins: Inner, Left, Right, Full, Cross",
                "Database Management Systems",
                Difficulty.EASY,
                Arrays.asList("SQL", "Joins"),
                "Describe the behavior of each major SQL JOIN type.",
                "1. INNER JOIN: Returns rows where a matching key exists in BOTH tables.\n2. LEFT (OUTER) JOIN: Returns all rows from the left table, plus matched rows from the right table (unmatched right columns contain NULL).\n3. RIGHT (OUTER) JOIN: Returns all rows from the right table, plus matched rows from the left table.\n4. FULL (OUTER) JOIN: Returns rows when there is a match in either left or right table, filling with NULLs where keys don't match.\n5. CROSS JOIN: Returns the Cartesian product (N x M rows) combining every row from table A with every row from table B.",
                "In DBMS engines, joins are executed using Nested Loop Join, Hash Join, or Merge Join depending on indexes and data size.",
                "SELECT e.name, d.dept_name\nFROM employees e\nLEFT JOIN departments d ON e.dept_id = d.id;"
        ));

        q.add(new Question(
                "DBMS-006",
                "SQL vs NoSQL Architectural Trade-offs",
                "Database Management Systems",
                Difficulty.EASY,
                Arrays.asList("NoSQL", "SQL", "Architecture"),
                "What are the key architectural differences between SQL and NoSQL databases?",
                "1. Schema: SQL has structured, rigid schemas with strict types. NoSQL (Document, Key-Value, Column-family, Graph) has dynamic or schema-less structures (JSON, BSON).\n2. Scaling: SQL scales primarily vertically (larger CPU/RAM), though sharding is possible. NoSQL is architected for horizontal scaling across distributed commodity nodes.\n3. Transactions: SQL provides strong ACID transactions. NoSQL often emphasizes BASE (Basically Available, Soft-state, Eventual consistency) under CAP theorem trade-offs.",
                "Choose SQL when data relationships are complex and transactions are critical. Choose NoSQL for rapid polymorphic schemas or high-throughput distributed writes.",
                "// SQL: PostgreSQL, MySQL. NoSQL: MongoDB, Redis, Cassandra"
        ));

        q.add(new Question(
                "DBMS-007",
                "Clustered vs Non-Clustered Indexes",
                "Database Management Systems",
                Difficulty.MEDIUM,
                Arrays.asList("Indexing", "Performance"),
                "What is the difference between a Clustered Index and a Non-Clustered Index?",
                "1. Clustered Index: Dictates the physical storage order of rows on disk. A table can have only ONE clustered index (typically the Primary Key). The leaf nodes of a clustered index contain the actual row data.\n2. Non-Clustered Index: Stored in a separate structure from the data rows. The leaf nodes contain index keys and pointers (row IDs or clustered index keys) back to the actual data rows. A table can have multiple non-clustered indexes.",
                "Covering Index: A non-clustered index that includes all columns requested by a query, allowing the engine to return results without a secondary table lookup.",
                "CREATE INDEX idx_cover ON orders(customer_id) INCLUDE (order_total, order_date);"
        ));

        q.add(new Question(
                "DBMS-008",
                "Optimistic vs Pessimistic Locking",
                "Database Management Systems",
                Difficulty.MEDIUM,
                Arrays.asList("Concurrency", "Locking"),
                "Compare Optimistic Locking and Pessimistic Locking strategies.",
                "1. Pessimistic Locking: Assumes conflicts will occur. Locks the row immediately upon reading (`SELECT ... FOR UPDATE`), preventing other transactions from modifying or reading it until the lock is released. Best for high-contention environments with frequent conflicts.\n2. Optimistic Locking: Assumes conflicts are rare. Does not lock records upon reading. Uses a `version` number or timestamp column. When updating, checks `WHERE id = ? AND version = current_version`. If no rows are updated, a collision occurred, and the transaction aborts or retries.",
                "Optimistic locking provides significantly higher throughput in web applications where users view records for long durations.",
                "UPDATE products\nSET quantity = quantity - 1, version = version + 1\nWHERE id = 42 AND version = 3;"
        ));

        q.add(new Question(
                "DBMS-009",
                "Database Sharding vs Partitioning",
                "Database Management Systems",
                Difficulty.HARD,
                Arrays.asList("Scalability", "Distributed Systems"),
                "Differentiate between Database Sharding and Horizontal Partitioning.",
                "Horizontal Partitioning splits a large table into multiple smaller tables (partitions) within the same database instance on a single server (e.g. partition by range of dates). The query engine handles routing transparently.\nSharding splits data horizontally across MULTIPLE independent database servers/nodes, each holding a subset of the dataset (shard). It requires a shard key, partition routing logic, and handles distributed joins and transactions with increased complexity.",
                "A poor shard key choice leads to 'hotspots', where one database node receives an unbalanced proportion of traffic.",
                "// Hash sharding: Shard = hash(userId) % numberOfNodes"
        ));

        q.add(new Question(
                "DBMS-010",
                "Query Execution Plan & Optimization",
                "Database Management Systems",
                Difficulty.MEDIUM,
                Arrays.asList("Performance", "SQL Tuning"),
                "How do you inspect and optimize a slow query using an Execution Plan?",
                "Use `EXPLAIN` or `EXPLAIN ANALYZE` before the SQL query.\nKey metrics to look for:\n1. Full Table Scan (Seq Scan): Engine scans every table page. Fix: Add an appropriate index.\n2. Index Scan vs Index Only Scan: Index Only is fastest because it reads entirely from cache without touching table pages.\n3. Filter efficiency and rows estimation: Large discrepancy between estimated rows and actual rows means table statistics are stale (run `ANALYZE`).\n4. Heavy temporary disk spills during sorting or hashing.",
                "Avoid leading wildcards in LIKE queries (`LIKE '%abc'`), as they prevent index range scans.",
                "EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'test@example.com';"
        ));

        // ==========================================
        // 6. OPERATING SYSTEMS (10 Questions)
        // ==========================================
        q.add(new Question(
                "OS-001",
                "Process vs Thread & Context Switching",
                "Operating Systems",
                Difficulty.EASY,
                Arrays.asList("Processes", "Threads", "Concurrency"),
                "Explain the difference between a process and a thread and what occurs during a context switch.",
                "A Process is an executing instance of a program with its own independent virtual address space, file descriptors, security credentials, and heap. Processes are isolated from each other.\nA Thread is the smallest unit of CPU scheduling within a process. Threads of the same process share the code segment, data segment, and heap, but each has its own private Stack and Program Counter (PC).\nA Context Switch saves the CPU state (registers, PC) of the running process/thread and restores that of another. Process context switches are significantly more expensive because they require invalidating the Translation Lookaside Buffer (TLB) due to address space switching.",
                "Threads allow cheap concurrent execution, but require synchronization to avoid race conditions on shared memory.",
                "// POSIX pthread_create() creates a new thread in the same process address space"
        ));

        q.add(new Question(
                "OS-002",
                "Deadlocks: Four Necessary Conditions & Banker's Algorithm",
                "Operating Systems",
                Difficulty.MEDIUM,
                Arrays.asList("Deadlocks", "Resource Allocation"),
                "What are the Coffman conditions for a deadlock, and how does Banker's Algorithm prevent it?",
                "Four conditions must hold simultaneously for a deadlock:\n1. Mutual Exclusion: At least one resource must be held in a non-shareable mode.\n2. Hold and Wait: A process holds at least one resource and requests additional resources held by others.\n3. No Preemption: Resources cannot be forcibly seized; only released voluntarily.\n4. Circular Wait: A closed chain of processes exists where each holds a resource needed by the next.\nBanker's Algorithm is a deadlock avoidance algorithm: it tests for safety by simulating the allocation of predetermined maximum possible amounts of resources, ensuring the system remains in a 'safe state' before granting requests.",
                "In practice, deadlocks are often avoided by imposing a global linear order on resource acquisitions.",
                "// Deadlock avoidance via ordering:\n// Thread 1 and Thread 2 both acquire lockA before lockB"
        ));

        q.add(new Question(
                "OS-003",
                "Virtual Memory, Paging, and Page Faults",
                "Operating Systems",
                Difficulty.HARD,
                Arrays.asList("Virtual Memory", "Paging", "MMU"),
                "Explain how Virtual Memory and Paging work, including what happens during a Page Fault.",
                "Virtual memory maps a process's logical address space to physical RAM via the Memory Management Unit (MMU) using Page Tables. Memory is divided into fixed-size blocks: Pages (logical) and Frames (physical, typically 4KB).\nA Page Fault occurs when a program accesses an address on a page not currently mapped in physical RAM (valid bit is 0):\n1. MMU triggers a page fault interrupt/trap to OS kernel.\n2. OS locates the required page in secondary storage (swap/disk).\n3. OS finds a free physical frame (or evicts a page using LRU/Clock replacement).\n4. Page is read from disk into the frame; page table is updated.\n5. CPU instruction is restarted transparently.",
                "Thrashing occurs when the system spends more time swapping pages in/out of disk than executing instructions due to insufficient physical memory.",
                "// Page Size typically 4096 bytes (4 KB)"
        ));

        q.add(new Question(
                "OS-004",
                "CPU Scheduling Algorithms",
                "Operating Systems",
                Difficulty.MEDIUM,
                Arrays.asList("CPU Scheduling", "Kernel"),
                "Compare Round Robin, Shortest Job First (SJF), and Multi-Level Feedback Queue (MLFQ) scheduling.",
                "1. Round Robin (RR): Each process gets a fixed time slice (quantum). Preemptive, fair, prevents starvation, but performance depends on quantum size (too small = excessive context switch overhead; too large = FCFS).\n2. Shortest Job First (SJF): Schedules process with smallest burst time. Mathematically optimal for minimizing average waiting time, but impractical because future CPU bursts are unknown and it can starve long processes.\n3. Multi-Level Feedback Queue (MLFQ): Uses multiple priority queues. Interactive jobs stay at high priority; CPU-bound jobs drop to lower priority queues with longer quantums. Adapts dynamically without prior knowledge of job lengths.",
                "Linux uses the Completely Fair Scheduler (CFS), based on Red-Black Trees tracking each process's virtual runtime (`vruntime`).",
                "// CFS picks the process with the smallest vruntime from the RB-Tree in O(log N)"
        ));

        q.add(new Question(
                "OS-005",
                "Inter-Process Communication (IPC) Mechanisms",
                "Operating Systems",
                Difficulty.MEDIUM,
                Arrays.asList("IPC", "Processes"),
                "Compare the major Inter-Process Communication (IPC) mechanisms in modern operating systems.",
                "1. Pipes / FIFOs: Unidirectional byte streams between related processes (or named pipes for unrelated processes).\n2. Shared Memory: Fastest IPC because processes map the same physical memory frames into their address spaces; requires semaphores/mutexes for synchronization.\n3. Message Queues: OS-maintained linked list of messages with typed priorities.\n4. Sockets: Network or Unix domain sockets. Works across networks or locally with high security and flexibility.\n5. Signals: Asynchronous notifications sent to a process (e.g. SIGINT, SIGKILL, SIGTERM).",
                "Shared memory has zero copy overhead, whereas pipes and sockets copy data between user space and kernel buffers.",
                "// POSIX shm_open() and mmap() establish shared memory segments"
        ));

        q.add(new Question(
                "OS-006",
                "Monolithic vs Microkernel Architecture",
                "Operating Systems",
                Difficulty.MEDIUM,
                Arrays.asList("OS Architecture", "Kernels"),
                "Compare Monolithic Kernel and Microkernel architectures.",
                "1. Monolithic Kernel (e.g. Linux, traditional Unix): All OS services (VFS, device drivers, network stack, IPC, scheduler, memory manager) run in the same privileged Kernel Space (Ring 0). Extremely fast due to direct function calls, but a single driver crash can bring down the entire system.\n2. Microkernel (e.g. QNX, seL4, Mach): Only essential primitives (minimal IPC, basic scheduling, virtual memory translation) run in kernel space. All drivers, filesystems, and network stacks run as isolated User Space daemons (Ring 3). Highly resilient and secure, but incurs IPC message-passing overhead.",
                "Hybrid Kernels (e.g. Windows NT, macOS XNU) run core subsystems in kernel space for speed while using modular abstractions.",
                "// Microkernel isolates driver faults: crash restarts only the driver process"
        ));

        q.add(new Question(
                "OS-007",
                "File System Inodes and Directory Structure",
                "Operating Systems",
                Difficulty.MEDIUM,
                Arrays.asList("FileSystem", "Storage"),
                "What is an Inode in Unix-like file systems and what does it contain?",
                "An Inode (index node) is a data structure on disk that stores metadata about a file or directory. An inode contains:\n- File type and permissions (rwx)\n- Owner UID and GID\n- File size in bytes\n- Timestamps (atime, mtime, ctime)\n- Number of hard links\n- Pointers to data blocks on disk (direct blocks, single indirect, double indirect, triple indirect, or extents).\nCrucially, an inode DOES NOT store the file's name or actual file contents. Directory entries map filenames to inode numbers.",
                "Hard links point to the existing inode number; soft (symbolic) links point to the file path.",
                "ls -i filename  # Displays inode number of the file"
        ));

        q.add(new Question(
                "OS-008",
                "Semaphores vs Mutexes",
                "Operating Systems",
                Difficulty.EASY,
                Arrays.asList("Synchronization", "Concurrency"),
                "What is the difference between a Mutex and a Semaphore?",
                "1. Mutex (Mutual Exclusion Object): A locking mechanism with ownership. Only the thread that acquired the mutex can unlock it. Designed strictly for serializing access to a shared critical section (binary).\n2. Counting Semaphore: A signaling mechanism based on an integer counter with two atomic operations: `wait()` (P) decrements, and `signal()` (V) increments. Can manage access to a finite pool of resources (e.g., connection pool). Any thread can signal a semaphore; there is no concept of ownership.",
                "Mutexes can support Priority Inheritance to prevent Priority Inversion; semaphores generally do not.",
                "Semaphore sem = new Semaphore(5); // permits up to 5 concurrent threads\nsem.acquire();\ntry { /* use resource */ } finally { sem.release(); }"
        ));

        q.add(new Question(
                "OS-009",
                "User Space vs Kernel Space & System Calls",
                "Operating Systems",
                Difficulty.EASY,
                Arrays.asList("Kernel", "Syscalls", "Security"),
                "Why do OS CPUs separate User Space and Kernel Space, and how are System Calls made?",
                "Hardware CPU privilege rings (Ring 3 for User Mode, Ring 0 for Kernel Mode) enforce fault isolation and security. User applications cannot directly execute privileged instructions (I/O, memory page mapping, hardware device control).\nWhen an application needs an OS service, it executes a System Call:\n1. Application sets syscall number and parameters in CPU registers.\n2. Executes a software interrupt/trap (e.g. `syscall` or `int 0x80`).\n3. CPU switches to Ring 0 and jumps to the kernel's system call dispatch table.\n4. Kernel validates arguments, performs the operation, and executes `sysret` back to Ring 3.",
                "System call context switches involve switching stacks and validating pointers to prevent privilege escalation.",
                "// C library wrappers (e.g. read(), write()) abstract low-level assembly syscall instructions"
        ));

        q.add(new Question(
                "OS-010",
                "Memory Leaks vs Dangling Pointers",
                "Operating Systems",
                Difficulty.EASY,
                Arrays.asList("Memory", "Pointers"),
                "Contrast a Memory Leak with a Dangling Pointer in native systems programming.",
                "1. Memory Leak: Occurs when dynamically allocated memory (heap) is no longer referenced or reachable by the program, but has not been deallocated (`free()` or `delete` was omitted). Causes gradual memory consumption until OOM.\n2. Dangling Pointer: Occurs when a pointer continues to reference a memory location after that memory has already been freed or after a local stack variable went out of scope. Dereferencing causes undefined behavior, segmentation faults, or security vulnerabilities (Use-After-Free).",
                "Modern languages prevent dangling pointers via managed runtimes (Java, Python) or compile-time ownership systems (Rust).",
                "int *ptr = malloc(sizeof(int));\nfree(ptr);\n// ptr is now a dangling pointer!\n*ptr = 10; // Dangerous Use-After-Free!"
        ));

        // ==========================================
        // 7. COMPUTER NETWORKS (10 Questions)
        // ==========================================
        q.add(new Question(
                "CN-001",
                "OSI Model 7 Layers vs TCP/IP Protocol Suite",
                "Computer Networks",
                Difficulty.EASY,
                Arrays.asList("OSI", "Protocols", "Basics"),
                "Name the 7 layers of the OSI model and map them to the 4-layer TCP/IP suite.",
                "OSI 7 Layers:\n7. Application (HTTP, DNS, SSH)\n6. Presentation (SSL/TLS, encoding, compression)\n5. Session (RPC, NetBIOS, session control)\n4. Transport (TCP, UDP)\n3. Network (IP, ICMP, routing)\n2. Data Link (Ethernet, MAC, switches, framing)\n1. Physical (Cables, fiber, bits, modulation).\n\nTCP/IP 4 Layers:\n- Application Layer (combines OSI 5, 6, 7)\n- Transport Layer (OSI 4)\n- Internet Layer (OSI 3)\n- Network Access / Link Layer (combines OSI 1, 2).",
                "Each layer adds its own header (encapsulation) when sending, and strips it (decapsulation) when receiving.",
                "// Data encapsulation: [Ethernet [IP [TCP [HTTP Payload]]]]"
        ));

        q.add(new Question(
                "CN-002",
                "TCP Three-Way Handshake and Connection Teardown",
                "Computer Networks",
                Difficulty.MEDIUM,
                Arrays.asList("TCP", "Handshake", "Transport"),
                "Describe the TCP 3-way handshake to establish a connection and 4-way handshake to terminate it.",
                "Connection Establishment (3-Way Handshake):\n1. Client -> Server: SYN (Synchronize sequence number, client ISN=X).\n2. Server -> Client: SYN-ACK (Server ISN=Y, ACK=X+1).\n3. Client -> Server: ACK (ACK=Y+1). Connection is ESTABLISHED.\n\nConnection Termination (4-Way Handshake):\n1. Active closer -> Passive: FIN.\n2. Passive -> Active closer: ACK.\n3. Passive -> Active closer: FIN.\n4. Active closer -> Passive: ACK.\nThe active closer enters TIME_WAIT state (2 * MSL) to ensure the final ACK was received and prevent old duplicate packets from interfering with new connections.",
                "SYN Flood attacks exploit the half-open connection state before the 3rd ACK; mitigated using SYN Cookies.",
                "// TCP guarantees reliable, ordered, byte-stream delivery with flow and congestion control"
        ));

        q.add(new Question(
                "CN-003",
                "TCP vs UDP: In-Depth Comparison",
                "Computer Networks",
                Difficulty.EASY,
                Arrays.asList("TCP", "UDP", "Transport"),
                "What are the structural and behavioral differences between TCP and UDP?",
                "1. TCP (Transmission Control Protocol):\n- Connection-oriented (requires 3-way handshake).\n- Reliable: uses acknowledgments, retransmissions, checksums.\n- Ordered: uses sequence numbers.\n- Flow control (Sliding Window) & Congestion control (Slow Start, Congestion Avoidance).\n- Higher overhead (20-60 byte header).\n2. UDP (User Datagram Protocol):\n- Connectionless (send and forget).\n- Unreliable: no ACKs, no retransmissions.\n- Unordered.\n- Lightweight (8 byte header), low latency.\n- Supports broadcasting and multicasting.",
                "Use TCP for web, file transfers, email (HTTP, FTP, SMTP). Use UDP for real-time gaming, VoIP, live streaming, DNS queries.",
                "// QUIC (HTTP/3) builds reliable multiplexed transport on top of UDP in user space"
        ));

        q.add(new Question(
                "CN-004",
                "DNS Lookup Resolution Step-by-Step",
                "Computer Networks",
                Difficulty.MEDIUM,
                Arrays.asList("DNS", "Internet", "Protocols"),
                "Trace what happens behind the scenes during a recursive DNS resolution for 'www.google.com'.",
                "1. Browser checks local cache -> OS checks hosts file & OS DNS cache.\n2. Request sent to Recursive Resolver (ISP or 8.8.8.8).\n3. Resolver queries Root DNS Server (.) -> Root returns IP of .com TLD Nameserver.\n4. Resolver queries .com TLD Nameserver -> returns Authoritative Nameserver for google.com.\n5. Resolver queries Authoritative Nameserver for google.com -> returns A record (IPv4 address, e.g. 142.250.190.46) or AAAA (IPv6).\n6. Resolver caches record according to TTL and returns IP to OS and browser.",
                "DNS predominantly uses UDP port 53 for speed, falling back to TCP port 53 for zone transfers or responses > 512 bytes.",
                "dig +trace www.google.com  # Traces full recursive DNS lookup hierarchy"
        ));

        q.add(new Question(
                "CN-005",
                "HTTPS Handshake & TLS 1.3 Improvements",
                "Computer Networks",
                Difficulty.HARD,
                Arrays.asList("Security", "HTTPS", "TLS"),
                "Explain how an HTTPS / TLS handshake works and how TLS 1.3 reduced latency.",
                "HTTPS combines HTTP with TLS (Transport Layer Security):\n1. ClientHello: Sends supported cipher suites, TLS version, and client random.\n2. ServerHello: Chooses cipher suite, sends server certificate (public key) signed by a trusted CA, and server random.\n3. Authentication: Client verifies certificate chain against OS Root Trust Store.\n4. Key Exchange: In TLS 1.3, uses Ephemeral Diffie-Hellman (ECDHE) for Perfect Forward Secrecy (PFS).\n5. Both parties derive symmetric session keys (AES-GCM or ChaCha20) for encrypting application data.\nTLS 1.3 improvement: Reduced the handshake from 2 round-trips (2-RTT) in TLS 1.2 to 1-RTT (or 0-RTT for resumed sessions).",
                "Symmetric encryption is used for data transmission because asymmetric encryption (RSA/ECC) is computationally too slow.",
                "// TLS 1.3 removes obsolete insecure ciphers like RSA key exchange and RC4"
        ));

        q.add(new Question(
                "CN-006",
                "HTTP/1.1 vs HTTP/2 vs HTTP/3",
                "Computer Networks",
                Difficulty.MEDIUM,
                Arrays.asList("HTTP", "Web", "Protocols"),
                "Compare HTTP/1.1, HTTP/2, and HTTP/3 protocol evolutions.",
                "1. HTTP/1.1: Text-based. Supports Keep-Alive persistent connections, but suffers from Head-of-Line (HoL) blocking at the application layer (only 1 request-response per TCP connection at a time; browsers open 6 connections per domain).\n2. HTTP/2: Binary protocol. Introduces Multiplexing: multiple bidirectional streams over a single TCP connection. Adds Header Compression (HPACK) and Server Push. However, still suffers from TCP-level HoL blocking (if 1 packet drops, all streams on that TCP connection wait).\n3. HTTP/3: Replaces TCP with QUIC (built on UDP). Solves TCP HoL blocking completely (packet loss on one stream does not stall independent streams) and enables 0-RTT handshakes and connection migration across networks.",
                "HTTP/3 eliminates the dual handshake (TCP + TLS) by combining transport and cryptographic negotiation into a single round trip.",
                "// HTTP/2: multiplexed binary frames over single TCP stream\n// HTTP/3: multiplexed independent streams over UDP/QUIC"
        ));

        q.add(new Question(
                "CN-007",
                "Subnetting, CIDR, and IP Addressing",
                "Computer Networks",
                Difficulty.MEDIUM,
                Arrays.asList("IP", "Subnetting", "CIDR"),
                "Explain CIDR notation (e.g. 192.168.1.0/24) and how subnet masks calculate network and broadcast addresses.",
                "CIDR (Classless Inter-Domain Routing) defines the number of leading 1-bits in the subnet mask. `/24` means the first 24 bits are the Network ID, and the remaining 8 bits (32 - 24 = 8) are the Host ID.\n- Total IP addresses: 2^8 = 256 addresses.\n- Usable host addresses: 256 - 2 = 254 (first address 192.168.1.0 is Network Address; last address 192.168.1.255 is Broadcast Address).\n- Subnet Mask: 255.255.255.0. A bitwise AND between an IP address and the subnet mask yields the Network Address.",
                "Subnetting limits broadcast domains, enhances security, and conserves scarce IPv4 address space.",
                "IP: 192.168.1.50 AND 255.255.255.0 = Network: 192.168.1.0"
        ));

        q.add(new Question(
                "CN-008",
                "NAT (Network Address Translation) & Port Forwarding",
                "Computer Networks",
                Difficulty.EASY,
                Arrays.asList("NAT", "Routing"),
                "How does NAT allow multiple devices on a private LAN to share a single public IP address?",
                "NAT (specifically NAPT / PAT - Port Address Translation) maps private RFC 1918 IP addresses (10.0.0.0/8, 172.16.0.0/12, 192.168.0.0/16) to a single public IP address.\nThe router maintains a NAT Translation Table:\nWhen a LAN device sends an outbound packet, the router replaces the private IP and source port with its own public IP and an available ephemeral port. When return packets arrive on that external port, the router translates destination back to the internal device's private IP/port.",
                "NAT solved the immediate IPv4 address exhaustion crisis in the 1990s without requiring immediate IPv6 migration.",
                "// LAN 192.168.1.10:54321 -> NAT Router Public 203.0.113.5:60001 -> Web Server"
        ));

        q.add(new Question(
                "CN-009",
                "TCP Flow Control vs Congestion Control",
                "Computer Networks",
                Difficulty.HARD,
                Arrays.asList("TCP", "Congestion", "Flow Control"),
                "Distinguish between TCP Flow Control and TCP Congestion Control.",
                "1. Flow Control: Protects the RECEIVER from being overwhelmed by a sender that transmits faster than the receiver can buffer and process. Implemented via the Receive Window (rwnd) advertised in TCP headers.\n2. Congestion Control: Protects the NETWORK infrastructure (routers, intermediate links) from being overwhelmed. Implemented via the Congestion Window (cwnd) maintained by the sender based on algorithms like Tahoe, Reno, Cubic, or BBR (Slow Start, Congestion Avoidance, Fast Retransmit, Fast Recovery).\nEffective transmission rate is: `min(rwnd, cwnd)`.",
                "Flow control is end-to-end; congestion control accounts for shared network topology limits.",
                "// Effective window = Math.min(receiverWindow, congestionWindow)"
        ));

        q.add(new Question(
                "CN-010",
                "CORS (Cross-Origin Resource Sharing) & Preflight",
                "Computer Networks",
                Difficulty.EASY,
                Arrays.asList("Web", "Security", "CORS"),
                "What is CORS and why do browsers send OPTIONS preflight requests?",
                "CORS is a browser security mechanism that allows or restricts web applications running at one origin (domain, protocol, port) from requesting resources from a different origin, enforcing the Same-Origin Policy (SOP).\nPreflight Request: For 'non-simple' HTTP requests (methods other than GET/POST/HEAD, or custom headers like Authorization), the browser automatically sends an HTTP `OPTIONS` request first asking the server for permission.\nThe server responds with headers like `Access-Control-Allow-Origin` and `Access-Control-Allow-Methods`.",
                "CORS is enforced by the CLIENT browser, not by the server; server-to-server or cURL calls ignore CORS.",
                "OPTIONS /api/data HTTP/1.1\nOrigin: https://myapp.com\nAccess-Control-Request-Method: POST"
        ));

        // ==========================================
        // 8. SOFTWARE ENGINEERING & SYSTEM DESIGN (10 Questions)
        // ==========================================
        q.add(new Question(
                "SE-001",
                "Monolith vs Microservices Architecture",
                "Software Engineering",
                Difficulty.MEDIUM,
                Arrays.asList("Architecture", "Microservices", "Design"),
                "Analyze the trade-offs between Monolithic and Microservices architectures.",
                "1. Monolithic:\n- Pros: Simple deployment, easy end-to-end testing, zero network latency between components, transactional consistency (ACID) across business modules.\n- Cons: Tight coupling, long build/deploy cycles, difficult to scale individual bottlenecks, technology lock-in.\n2. Microservices:\n- Pros: Independent deployments, autonomous teams, localized scaling, freedom of tech stack per service, fault isolation.\n- Cons: Distributed system complexity (network failures, data consistency/Saga patterns, distributed tracing, API gateways, deployment overhead with Kubernetes).",
                "Start with a well-modularized monolith; split into microservices only when team scale or specific scalability bottlenecks demand it.",
                "// Monolith: single deployable JAR/WAR\n// Microservices: independent Docker containers communicating via REST/gRPC/Kafka"
        ));

        q.add(new Question(
                "SE-002",
                "CI/CD Pipelines: Continuous Integration vs Delivery vs Deployment",
                "Software Engineering",
                Difficulty.EASY,
                Arrays.asList("DevOps", "CI/CD", "Testing"),
                "Clarify the differences between Continuous Integration, Continuous Delivery, and Continuous Deployment.",
                "1. Continuous Integration (CI): Developers regularly merge code into a shared repository. Every push triggers automated builds, linting, and unit/integration tests to detect bugs early.\n2. Continuous Delivery (CD): Automated pipeline packages and tests artifacts so they are always in a releasable state. Deployment to production requires a manual approval gate.\n3. Continuous Deployment: Every change that passes automated CI/CD stages is automatically deployed directly to production without any human intervention.",
                "Robust automated test suites (unit, integration, regression, smoke tests) are mandatory for safe Continuous Deployment.",
                "// Pipeline stages: Commit -> Build -> Unit Tests -> Integration Tests -> Deploy Staging -> Deploy Prod"
        ));

        q.add(new Question(
                "SE-003",
                "API Design: RESTful Best Practices and Idempotency",
                "Software Engineering",
                Difficulty.MEDIUM,
                Arrays.asList("API", "REST", "HTTP"),
                "What defines a RESTful API and which HTTP methods are idempotent?",
                "RESTful design represents resources as URIs using nouns (`/users`, `/orders/123/items`), not verbs.\nStandard HTTP verbs:\n- GET: Safe and idempotent (fetches resource; no side effects).\n- POST: Neither safe nor idempotent (creates a new resource).\n- PUT: Idempotent (replaces entire resource representation).\n- PATCH: Typically not idempotent unless specified (partial updates).\n- DELETE: Idempotent (deleting resource multiple times leaves system in same state: resource absent).\nAn operation is idempotent if executing it once has the exact same effect as executing it multiple times.",
                "Use HTTP status codes appropriately: 200 (OK), 201 (Created), 204 (No Content), 400 (Bad Request), 401 (Unauthorized), 404 (Not Found), 409 (Conflict), 500 (Server Error).",
                "POST /api/v1/orders         # Create order (non-idempotent)\nPUT  /api/v1/users/42       # Replace user 42 (idempotent)"
        ));

        q.add(new Question(
                "SE-004",
                "Agile vs Waterfall Methodology",
                "Software Engineering",
                Difficulty.EASY,
                Arrays.asList("Agile", "Scrum", "Process"),
                "Compare the Waterfall development lifecycle with Agile Scrum methodology.",
                "1. Waterfall: Sequential, rigid phases (Requirements -> Design -> Implementation -> Verification -> Maintenance). Minimal client interaction until late. High risk if requirements change mid-project.\n2. Agile (Scrum): Iterative and incremental development in short time-boxed Sprints (typically 1-3 weeks). Cross-functional teams, continuous stakeholder feedback, daily standups, sprint reviews, and retrospectives. Embraces changing requirements.",
                "Waterfall works when requirements are immutable and compliance-critical (aerospace, medical devices). Agile is optimal for dynamic product development.",
                "// Scrum Ceremonies: Sprint Planning, Daily Standup, Sprint Review, Retrospective"
        ));

        q.add(new Question(
                "SE-005",
                "CAP Theorem and PACELC in Distributed Systems",
                "Software Engineering",
                Difficulty.HARD,
                Arrays.asList("Distributed Systems", "CAP", "Consistency"),
                "Explain the CAP Theorem and how PACELC extends it.",
                "CAP Theorem states that a distributed data store can guarantee at most two of the three properties:\n- Consistency (C): Every read receives the most recent write or an error.\n- Availability (A): Every non-failing node returns a non-error response.\n- Partition Tolerance (P): System operates despite dropped or delayed network messages between nodes.\nSince network partitions (P) are inevitable in real networks, distributed systems must choose CP (e.g. HBase, Zookeeper) or AP (e.g. Cassandra, DynamoDB).\nPACELC Theorem extends CAP: If there is a Partition (P), how does system trade Availability (A) vs Consistency (C); Else (E), how does system trade Latency (L) vs Consistency (C)?",
                "MongoDB is PC/EC (consistent under partition, consistent in normal state). Cassandra is PA/EL (available under partition, low latency in normal state).",
                "// Partition: Choose between Consistency (wait for sync) or Availability (return stale data)"
        ));

        q.add(new Question(
                "SE-006",
                "Caching Strategies: Cache-Aside vs Write-Through vs Write-Back",
                "Software Engineering",
                Difficulty.MEDIUM,
                Arrays.asList("Caching", "Performance", "System Design"),
                "Compare Cache-Aside, Write-Through, and Write-Back (Write-Behind) caching patterns.",
                "1. Cache-Aside (Lazy Loading): Application queries cache first. On miss, reads from DB, populates cache, and returns. Simple and resilient to cache crashes, but initial reads suffer cache-miss latency.\n2. Write-Through: Application writes data to the cache, and the cache synchronously writes to the database before returning success. Guarantees consistency, but write latency is higher.\n3. Write-Back (Write-Behind): Application writes data to the cache only. The cache asynchronously batches writes to the database. Extremely low write latency, but data loss can occur if the cache node crashes before flushing to disk.",
                "Always set Time-To-Live (TTL) on cached keys to prevent stale data buildup.",
                "// Cache-aside read pattern:\nData d = cache.get(key);\nif (d == null) {\n    d = db.find(key);\n    cache.put(key, d, ttl);\n}\nreturn d;"
        ));

        q.add(new Question(
                "SE-007",
                "Circuit Breaker Pattern in Resilient Distributed Systems",
                "Software Engineering",
                Difficulty.HARD,
                Arrays.asList("Design Patterns", "Resilience", "Microservices"),
                "How does the Circuit Breaker pattern prevent cascading failures in microservices?",
                "When a downstream dependency fails or times out, repeatedly calling it exhausts thread pools and causes cascading failures across the entire system. The Circuit Breaker wraps the remote call and maintains three states:\n1. Closed: Normal operation; requests pass through. Tracks failure rate.\n2. Open: When failures cross a threshold (e.g. 50% in 10s), the breaker trips to Open. Subsequent calls fail immediately or return fallback responses without making network calls.\n3. Half-Open: After a sleep window, permits a limited number of trial requests. If successful, resets to Closed; if failing, reverts to Open.",
                "Popular implementations include Resilience4j, Netflix Hystrix, and Envoy proxy service meshes.",
                "// Circuit Breaker: Closed -> (failure threshold exceeded) -> Open -> (cooldown) -> Half-Open"
        ));

        q.add(new Question(
                "SE-008",
                "Code Smells and Refactoring Techniques",
                "Software Engineering",
                Difficulty.EASY,
                Arrays.asList("Clean Code", "Refactoring"),
                "Identify 4 common code smells and their corresponding refactoring solutions.",
                "1. Long Method: A method doing too much. Solution: Extract Method into smaller, focused helper methods.\n2. Large Class (God Object): Class violating SRP with too many responsibilities. Solution: Extract Class or interface segregation.\n3. Feature Envy: A method accesses the data of another object more than its own. Solution: Move Method into the object containing the data.\n4. Primitive Obsession: Using primitive types instead of small domain objects (e.g., using String for zip code, phone number, currency). Solution: Introduce Value Object.",
                "Refactoring must be supported by comprehensive automated unit tests to ensure behavior remains identical.",
                "// Primitive obsession refactoring: String email -> new EmailAddress(email)"
        ));

        q.add(new Question(
                "SE-009",
                "Rate Limiting Algorithms: Token Bucket vs Leaky Bucket",
                "Software Engineering",
                Difficulty.HARD,
                Arrays.asList("System Design", "Rate Limiting"),
                "Compare the Token Bucket and Leaky Bucket rate limiting algorithms.",
                "1. Token Bucket: Tokens are added to a bucket of capacity B at a constant rate R. A request consumes 1 token. If tokens are available, the request proceeds immediately; if empty, it is dropped or throttled. Advantage: Allows controlled bursts of traffic up to capacity B while maintaining an average rate R.\n2. Leaky Bucket: Requests enter a FIFO queue (the bucket) of capacity B. Requests are processed at a strictly constant output rate R. Advantage: Completely smooths out traffic into a constant flow. Disadvantage: Bursty traffic gets delayed in the queue even if system has capacity.",
                "Token bucket is the industry standard (used in AWS API Gateway, NGINX, Stripe) because it accommodates reasonable client bursts.",
                "// Token bucket: tokens = Math.min(capacity, tokens + elapsedTime * refillRate)"
        ));

        q.add(new Question(
                "SE-010",
                "Message Queues: At-Least-Once vs Exactly-Once Delivery",
                "Software Engineering",
                Difficulty.MEDIUM,
                Arrays.asList("Messaging", "Kafka", "Distributed Systems"),
                "Why is 'Exactly-Once' delivery difficult in distributed systems, and how is it practically achieved?",
                "Network unreliability makes true exactly-once delivery across independent network boundaries mathematically impossible without coordination (Two Generals Problem). In practice:\n- Most message brokers guarantee At-Least-Once delivery (re-delivers message if acknowledgment packet drops).\nTo achieve effective exactly-once processing:\n1. Consumer Idempotency: Designing consumers so processing a duplicate message produces identical side effects (e.g. `UPSERT` instead of `INSERT`, storing processed message IDs in a dedup table).\n2. Transactional Messaging: Apache Kafka achieves transactional exactly-once semantics (EOS) across Kafka-to-Kafka pipelines using idempotent producers (PID + sequence numbers) and 2-phase commit transaction coordinators.",
                "Designing consumers to be idempotent is the most robust and universal approach in enterprise distributed systems.",
                "// Idempotent processing with unique constraint:\n// INSERT INTO processed_events(event_id) VALUES (?) ON CONFLICT DO NOTHING;"
        ));

        return q;
    }

    public static List<QuizQuestion> createDefaultQuizQuestions() {
        List<QuizQuestion> list = new ArrayList<>();

        // Java MCQs
        list.add(new QuizQuestion(
                "QZ-JAVA-01", "Java Programming", Difficulty.EASY,
                "Which memory area in the JVM is shared among all executing threads?",
                Arrays.asList("JVM Stack", "Heap Memory", "Program Counter Register", "Native Method Stack"),
                1,
                "Heap Memory and Method Area (Metaspace) are shared among all threads, whereas Stacks and PC Registers are thread-private."
        ));

        list.add(new QuizQuestion(
                "QZ-JAVA-02", "Java Programming", Difficulty.MEDIUM,
                "What is the time complexity of searching a key in a Java 8 HashMap with a heavily collided bucket containing 64 items?",
                Arrays.asList("O(1)", "O(N)", "O(log N)", "O(N^2)"),
                2,
                "In Java 8+, when a bucket exceeds 8 nodes and total capacity is >= 64, it treeifies into a Red-Black Tree, yielding O(log N) search."
        ));

        list.add(new QuizQuestion(
                "QZ-JAVA-03", "Java Programming", Difficulty.HARD,
                "What does the volatile keyword in Java guarantee?",
                Arrays.asList("Atomicity only", "Mutual exclusion and locking", "Memory visibility and prevents instruction reordering", "Both atomicity and lock acquisition"),
                2,
                "Volatile guarantees visibility of writes across threads and establishes a happens-before order, but does not provide atomicity for compound operations."
        ));

        list.add(new QuizQuestion(
                "QZ-JAVA-04", "Java Programming", Difficulty.EASY,
                "Which of the following is a checked exception in Java?",
                Arrays.asList("NullPointerException", "ArrayIndexOutOfBoundsException", "IOException", "IllegalArgumentException"),
                2,
                "IOException extends java.lang.Exception directly and must be declared or caught, whereas the others inherit from RuntimeException."
        ));

        list.add(new QuizQuestion(
                "QZ-JAVA-05", "Java Programming", Difficulty.MEDIUM,
                "What happens to generic type parameters at runtime in Java bytecode?",
                Arrays.asList("They are preserved as reflection metadata only", "They are erased and replaced with their bounds or Object", "They become C++ templates", "They cause runtime verification errors"),
                1,
                "Java utilizes Type Erasure to maintain backward compatibility, replacing type parameters with Object or the first upper bound."
        ));

        // Python MCQs
        list.add(new QuizQuestion(
                "QZ-PY-01", "Python Programming", Difficulty.MEDIUM,
                "What is the primary consequence of CPython's Global Interpreter Lock (GIL)?",
                Arrays.asList("Python programs cannot use network sockets", "Only one thread executes Python bytecode at a time in a process", "Memory cannot be dynamically allocated", "Functions cannot be passed as arguments"),
                1,
                "The GIL ensures that only one native OS thread executes Python bytecode at a time, limiting multi-threaded CPU-bound parallelism."
        ));

        list.add(new QuizQuestion(
                "QZ-PY-02", "Python Programming", Difficulty.EASY,
                "What is the output of `bool([])`, `bool({})`, and `bool(0)` in Python?",
                Arrays.asList("True, True, True", "False, False, False", "True, False, False", "None, None, None"),
                1,
                "In Python, empty collections (lists, dicts, tuples, sets) and numeric zero evaluate to falsy."
        ));

        list.add(new QuizQuestion(
                "QZ-PY-03", "Python Programming", Difficulty.MEDIUM,
                "Which statement about Python generators is TRUE?",
                Arrays.asList("Generators compute and store all values in RAM immediately", "Generators maintain their state between yield statements using O(1) memory", "Generators cannot be iterated over in a for loop", "Generators must return a list"),
                1,
                "Generators evaluate items lazily on demand when `next()` is called, using minimal constant memory."
        ));

        // DSA MCQs
        list.add(new QuizQuestion(
                "QZ-DSA-01", "Data Structures & Algorithms", Difficulty.MEDIUM,
                "What is the worst-case time complexity of QuickSort when the pivot is poorly chosen?",
                Arrays.asList("O(N log N)", "O(N)", "O(N^2)", "O(2^N)"),
                2,
                "When the array is already sorted or reverse sorted and the first or last element is chosen as pivot, QuickSort degenerates to O(N^2)."
        ));

        list.add(new QuizQuestion(
                "QZ-DSA-02", "Data Structures & Algorithms", Difficulty.EASY,
                "Which data structure is most appropriate for implementing an Undo/Redo feature?",
                Arrays.asList("Queue", "Stack", "Binary Tree", "Hash Set"),
                1,
                "Stacks operate on Last-In-First-Out (LIFO) semantics, making them ideal for tracking historical operations for Undo and Redo."
        ));

        list.add(new QuizQuestion(
                "QZ-DSA-03", "Data Structures & Algorithms", Difficulty.HARD,
                "Which algorithm finds the single-source shortest path on a directed graph with negative edge weights (no negative cycles)?",
                Arrays.asList("Dijkstra's Algorithm", "Floyd-Warshall", "Bellman-Ford Algorithm", "Kruskal's Algorithm"),
                2,
                "Bellman-Ford correctly handles negative edge weights by relaxing all edges V-1 times, whereas Dijkstra requires non-negative weights."
        ));

        list.add(new QuizQuestion(
                "QZ-DSA-04", "Data Structures & Algorithms", Difficulty.MEDIUM,
                "Kadane's algorithm is used to solve which problem in O(N) time?",
                Arrays.asList("Longest Increasing Subsequence", "Maximum Subarray Sum", "Shortest Path in DAG", "Matrix Multiplication"),
                1,
                "Kadane's dynamic programming algorithm finds the contiguous subarray with the largest sum in a single linear pass."
        ));

        // DBMS MCQs
        list.add(new QuizQuestion(
                "QZ-DBMS-01", "Database Management Systems", Difficulty.MEDIUM,
                "Which transaction isolation level allows Dirty Reads?",
                Arrays.asList("Serializable", "Repeatable Read", "Read Committed", "Read Uncommitted"),
                3,
                "Read Uncommitted permits transactions to read data that has been modified by another transaction but not yet committed."
        ));

        list.add(new QuizQuestion(
                "QZ-DBMS-02", "Database Management Systems", Difficulty.EASY,
                "Why do relational databases prefer B+ Trees over standard B-Trees for indexes?",
                Arrays.asList("B+ Trees store records in random order", "B+ Trees store data only in leaf nodes, maximizing fan-out and range scans", "B+ Trees do not require disk I/O", "B+ Trees have fewer leaves"),
                1,
                "B+ tree internal nodes contain only keys, allowing high branching factor. Linked leaf nodes make sequential range scans very fast."
        ));

        list.add(new QuizQuestion(
                "QZ-DBMS-03", "Database Management Systems", Difficulty.MEDIUM,
                "What does the 'A' in ACID represent?",
                Arrays.asList("Availability", "Atomicity", "Accuracy", "Asynchronous"),
                1,
                "Atomicity ensures that all operations in a transaction succeed completely or none do ('all or nothing')."
        ));

        // OS MCQs
        list.add(new QuizQuestion(
                "QZ-OS-01", "Operating Systems", Difficulty.EASY,
                "Which of the following is NOT a necessary condition for Deadlock?",
                Arrays.asList("Mutual Exclusion", "Hold and Wait", "Preemption of resources", "Circular Wait"),
                2,
                "The Coffman condition is 'NO Preemption' (resources cannot be forcibly taken away). Preemption prevents deadlock."
        ));

        list.add(new QuizQuestion(
                "QZ-OS-02", "Operating Systems", Difficulty.MEDIUM,
                "What component translates virtual addresses into physical memory addresses at hardware speed?",
                Arrays.asList("ALU", "MMU (Memory Management Unit)", "DMA Controller", "Kernel Dispatcher"),
                1,
                "The MMU uses page tables and the Translation Lookaside Buffer (TLB) cache to translate virtual to physical addresses."
        ));

        // Computer Networks MCQs
        list.add(new QuizQuestion(
                "QZ-CN-01", "Computer Networks", Difficulty.EASY,
                "At which layer of the OSI model does TCP operate?",
                Arrays.asList("Network Layer", "Data Link Layer", "Transport Layer", "Session Layer"),
                2,
                "TCP and UDP are Transport Layer (Layer 4) protocols that provide end-to-end communication."
        ));

        list.add(new QuizQuestion(
                "QZ-CN-02", "Computer Networks", Difficulty.MEDIUM,
                "What is the purpose of the TIME_WAIT state in TCP connection teardown?",
                Arrays.asList("To wait for new incoming connections", "To ensure the final ACK is received and old duplicate segments expire", "To compress transferred data", "To authenticate user credentials"),
                1,
                "TIME_WAIT persists for 2 * MSL (Maximum Segment Lifetime) so delayed packets from the old connection do not corrupt a new connection on the same port."
        ));

        // Software Engineering MCQs
        list.add(new QuizQuestion(
                "QZ-SE-01", "Software Engineering", Difficulty.MEDIUM,
                "According to the Liskov Substitution Principle (LSP), what must be true?",
                Arrays.asList("Classes must have only one public method", "Subclasses must be substitutable for their superclasses without breaking correctness", "Inheritance must always be preferred over composition", "Variables must be static"),
                1,
                "LSP states that objects of a superclass should be replaceable with objects of a subclass without affecting application correctness."
        ));

        list.add(new QuizQuestion(
                "QZ-SE-02", "Software Engineering", Difficulty.EASY,
                "Which HTTP method is designed to be idempotent according to the HTTP specification?",
                Arrays.asList("POST", "PUT", "CONNECT", "PATCH (in standard definition)"),
                1,
                "PUT replaces a resource entirely. Executing identical PUT requests multiple times results in the same state, making it idempotent."
        ));

        return list;
    }

    public static List<CodingChallenge> createDefaultCodingChallenges() {
        List<CodingChallenge> challenges = new ArrayList<>();

        // 1. Reverse an Array
        challenges.add(new CodingChallenge(
                "COD-001",
                "Reverse an Array",
                "Data Structures & Algorithms",
                Difficulty.EASY,
                "Given an array of integers, reverse the elements in-place and return the reversed array.\n\nExample 1:\nInput: [1, 2, 3, 4, 5]\nOutput: [5, 4, 3, 2, 1]\n\nExample 2:\nInput: [10, 20]\nOutput: [20, 10]",
                "An integer array nums.",
                "The reversed integer array nums.",
                "1 <= nums.length <= 10^5\n-10^9 <= nums[i] <= 10^9",
                "public class Solution {\n    public int[] reverseArray(int[] nums) {\n        // Write your solution here\n        int left = 0, right = nums.length - 1;\n        while (left < right) {\n            int temp = nums[left];\n            nums[left] = nums[right];\n            nums[right] = temp;\n            left++;\n            right--;\n        }\n        return nums;\n    }\n}",
                "Two pointers approach: swap elements at left and right pointers moving towards the center. O(N) time, O(1) space.",
                Arrays.asList(
                        new TestCase("1, 2, 3, 4, 5", "[5, 4, 3, 2, 1]", "Standard odd-length array", false),
                        new TestCase("10, 20", "[20, 10]", "Two elements array", false),
                        new TestCase("7", "[7]", "Single element array", false),
                        new TestCase("4, 1, 9, -2", "[-2, 9, 1, 4]", "Array with negative numbers", true)
                ),
                Arrays.asList("Array", "Two Pointers", "In-Place")
        ));

        // 2. Two Sum
        challenges.add(new CodingChallenge(
                "COD-002",
                "Two Sum",
                "Data Structures & Algorithms",
                Difficulty.EASY,
                "Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to target.\nAssume each input has exactly one solution, and do not use the same element twice.\n\nExample 1:\nInput: nums = [2, 7, 11, 15], target = 9\nOutput: [0, 1]\nExplanation: nums[0] + nums[1] == 9",
                "nums: array of integers, target: integer",
                "Indices [i, j] of the two numbers.",
                "2 <= nums.length <= 10^4\n-10^9 <= nums[i] <= 10^9\nOnly one valid answer exists.",
                "public class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        java.util.Map<Integer, Integer> map = new java.util.HashMap<>();\n        for (int i = 0; i < nums.length; i++) {\n            int complement = target - nums[i];\n            if (map.containsKey(complement)) {\n                return new int[] { map.get(complement), i };\n            }\n            map.put(nums[i], i);\n        }\n        return new int[0];\n    }\n}",
                "Use a Hash Map storing value -> index. For each number x, check if (target - x) is in the map. O(N) time, O(N) space.",
                Arrays.asList(
                        new TestCase("nums = [2, 7, 11, 15], target = 9", "[0, 1]", "Target 9 with first two elements", false),
                        new TestCase("nums = [3, 2, 4], target = 6", "[1, 2]", "Target 6 with 2 and 4", false),
                        new TestCase("nums = [3, 3], target = 6", "[0, 1]", "Identical elements", false),
                        new TestCase("nums = [-1, -2, -3, -4, -5], target = -8", "[2, 4]", "Negative values", true)
                ),
                Arrays.asList("Hash Table", "Array")
        ));

        // 3. Valid Palindrome
        challenges.add(new CodingChallenge(
                "COD-003",
                "Valid Palindrome",
                "Data Structures & Algorithms",
                Difficulty.EASY,
                "A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward.\n\nExample 1:\nInput: s = \"A man, a plan, a canal: Panama\"\nOutput: true\nExplanation: \"amanaplanacanalpanama\" is a palindrome.\n\nExample 2:\nInput: s = \"race a car\"\nOutput: false",
                "A string s.",
                "true if s is a valid palindrome, false otherwise.",
                "1 <= s.length() <= 2 * 10^5\ns consists only of printable ASCII characters.",
                "public class Solution {\n    public boolean isPalindrome(String s) {\n        int left = 0, right = s.length() - 1;\n        while (left < right) {\n            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) left++;\n            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;\n            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {\n                return false;\n            }\n            left++;\n            right--;\n        }\n        return true;\n    }\n}",
                "Two pointers from outside inward, skipping non-alphanumeric characters. O(N) time, O(1) space.",
                Arrays.asList(
                        new TestCase("\"A man, a plan, a canal: Panama\"", "true", "Classic Panama palindrome", false),
                        new TestCase("\"race a car\"", "false", "Not a palindrome", false),
                        new TestCase("\" \"", "true", "Empty / whitespace string", false),
                        new TestCase("\"0P\"", "false", "Alphanumeric mismatch", true)
                ),
                Arrays.asList("Two Pointers", "String")
        ));

        // 4. Maximum Subarray Sum (Kadane's Algorithm)
        challenges.add(new CodingChallenge(
                "COD-004",
                "Maximum Subarray Sum (Kadane's Algorithm)",
                "Data Structures & Algorithms",
                Difficulty.MEDIUM,
                "Given an integer array `nums`, find the subarray with the largest sum, and return its sum.\n\nExample 1:\nInput: nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]\nOutput: 6\nExplanation: The subarray [4, -1, 2, 1] has the largest sum 6.",
                "An array of integers nums.",
                "The maximum subarray sum.",
                "1 <= nums.length <= 10^5\n-10^4 <= nums[i] <= 10^4",
                "public class Solution {\n    public int maxSubArray(int[] nums) {\n        int currentSum = nums[0];\n        int maxSum = nums[0];\n        for (int i = 1; i < nums.length; i++) {\n            currentSum = Math.max(nums[i], currentSum + nums[i]);\n            maxSum = Math.max(maxSum, currentSum);\n        }\n        return maxSum;\n    }\n}",
                "Kadane's dynamic programming algorithm: at each index, decide whether to extend the current subarray or start fresh. O(N) time, O(1) space.",
                Arrays.asList(
                        new TestCase("[-2, 1, -3, 4, -1, 2, 1, -5, 4]", "6", "Mixed positive and negative array", false),
                        new TestCase("[1]", "1", "Single element", false),
                        new TestCase("[5, 4, -1, 7, 8]", "23", "Mostly positive array", false),
                        new TestCase("[-5, -2, -9, -1]", "-1", "All negative numbers", true)
                ),
                Arrays.asList("Dynamic Programming", "Array", "Kadane")
        ));

        // 5. Move Zeroes
        challenges.add(new CodingChallenge(
                "COD-005",
                "Move Zeroes",
                "Data Structures & Algorithms",
                Difficulty.EASY,
                "Given an integer array `nums`, move all 0's to the end of it while maintaining the relative order of the non-zero elements in-place.\n\nExample 1:\nInput: nums = [0, 1, 0, 3, 12]\nOutput: [1, 3, 12, 0, 0]",
                "An array of integers nums.",
                "The modified array nums.",
                "1 <= nums.length <= 10^4\n-2^31 <= nums[i] <= 2^31 - 1",
                "public class Solution {\n    public int[] moveZeroes(int[] nums) {\n        int insertPos = 0;\n        for (int num : nums) {\n            if (num != 0) nums[insertPos++] = num;\n        }\n        while (insertPos < nums.length) {\n            nums[insertPos++] = 0;\n        }\n        return nums;\n    }\n}",
                "Two pointers: first pass copies all non-zero elements forward; second pass fills the remainder with zeroes. O(N) time, O(1) space.",
                Arrays.asList(
                        new TestCase("[0, 1, 0, 3, 12]", "[1, 3, 12, 0, 0]", "Multiple zeroes in array", false),
                        new TestCase("[0]", "[0]", "Single zero", false),
                        new TestCase("[1, 2, 3]", "[1, 2, 3]", "No zeroes", false),
                        new TestCase("[0, 0, 0, 5]", "[5, 0, 0, 0]", "Leading zeroes", true)
                ),
                Arrays.asList("Array", "Two Pointers")
        ));

        // 6. Binary Search
        challenges.add(new CodingChallenge(
                "COD-006",
                "Binary Search",
                "Data Structures & Algorithms",
                Difficulty.EASY,
                "Given an array of integers `nums` which is sorted in ascending order, and an integer `target`, write a function to search `target` in `nums`.\nIf `target` exists, return its index. Otherwise, return -1.\nYou must write an algorithm with O(log n) runtime complexity.\n\nExample 1:\nInput: nums = [-1, 0, 3, 5, 9, 12], target = 9\nOutput: 4",
                "nums: sorted array of integers, target: integer",
                "The index of target if found, else -1.",
                "1 <= nums.length <= 10^4\nAll integers in nums are unique and sorted in ascending order.",
                "public class Solution {\n    public int search(int[] nums, int target) {\n        int low = 0, high = nums.length - 1;\n        while (low <= high) {\n            int mid = low + (high - low) / 2;\n            if (nums[mid] == target) return mid;\n            else if (nums[mid] < target) low = mid + 1;\n            else high = mid - 1;\n        }\n        return -1;\n    }\n}",
                "Standard binary search halving the search space each step. O(log N) time, O(1) space.",
                Arrays.asList(
                        new TestCase("nums = [-1, 0, 3, 5, 9, 12], target = 9", "4", "Target present in array", false),
                        new TestCase("nums = [-1, 0, 3, 5, 9, 12], target = 2", "-1", "Target absent from array", false),
                        new TestCase("nums = [5], target = 5", "0", "Single element found", false),
                        new TestCase("nums = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], target = 1", "0", "Target at index 0", true)
                ),
                Arrays.asList("Binary Search", "Array")
        ));

        // 7. Container With Most Water (Two Pointers)
        challenges.add(new CodingChallenge(
                "COD-007",
                "Container With Most Water",
                "Data Structures & Algorithms",
                Difficulty.MEDIUM,
                "You are given an integer array `height` of length n. There are n vertical lines drawn such that the two endpoints of the i-th line are (i, 0) and (i, height[i]).\nFind two lines that together with the x-axis form a container, such that the container contains the most water.\nReturn the maximum amount of water a container can store.\n\nExample 1:\nInput: height = [1, 8, 6, 2, 5, 4, 8, 3, 7]\nOutput: 49",
                "An array of integers height.",
                "Maximum area of water trapped.",
                "n == height.length\n2 <= n <= 10^5\n0 <= height[i] <= 10^4",
                "public class Solution {\n    public int maxArea(int[] height) {\n        int left = 0, right = height.length - 1;\n        int maxWater = 0;\n        while (left < right) {\n            int currentWater = Math.min(height[left], height[right]) * (right - left);\n            maxWater = Math.max(maxWater, currentWater);\n            if (height[left] < height[right]) left++;\n            else right--;\n        }\n        return maxWater;\n    }\n}",
                "Two pointers at ends: calculate area, then advance the pointer with smaller height because the height of the container is bounded by the shorter line. O(N) time, O(1) space.",
                Arrays.asList(
                        new TestCase("[1, 8, 6, 2, 5, 4, 8, 3, 7]", "49", "Standard test case", false),
                        new TestCase("[1, 1]", "1", "Minimum length array", false),
                        new TestCase("[4, 3, 2, 1, 4]", "16", "Symmetric heights", false),
                        new TestCase("[1, 2, 1]", "2", "Peak in middle", true)
                ),
                Arrays.asList("Two Pointers", "Greedy", "Array")
        ));

        // 8. Longest Substring Without Repeating Characters (Sliding Window)
        challenges.add(new CodingChallenge(
                "COD-008",
                "Longest Substring Without Repeating Characters",
                "Data Structures & Algorithms",
                Difficulty.MEDIUM,
                "Given a string `s`, find the length of the longest substring without repeating characters.\n\nExample 1:\nInput: s = \"abcabcbb\"\nOutput: 3\nExplanation: The answer is \"abc\", with length 3.\n\nExample 2:\nInput: s = \"bbbbb\"\nOutput: 1",
                "A string s.",
                "Length of longest substring without repeating characters.",
                "0 <= s.length <= 5 * 10^4\ns consists of English letters, digits, symbols and spaces.",
                "public class Solution {\n    public int lengthOfLongestSubstring(String s) {\n        java.util.Map<Character, Integer> lastSeen = new java.util.HashMap<>();\n        int maxLen = 0, left = 0;\n        for (int right = 0; right < s.length(); right++) {\n            char c = s.charAt(right);\n            if (lastSeen.containsKey(c)) {\n                left = Math.max(left, lastSeen.get(c) + 1);\n            }\n            lastSeen.put(c, right);\n            maxLen = Math.max(maxLen, right - left + 1);\n        }\n        return maxLen;\n    }\n}",
                "Sliding window pattern: keep right pointer moving forward, update left pointer to jump past the last occurrence of duplicate character. O(N) time, O(min(m, n)) space.",
                Arrays.asList(
                        new TestCase("\"abcabcbb\"", "3", "Repeating pattern", false),
                        new TestCase("\"bbbbb\"", "1", "All identical characters", false),
                        new TestCase("\"pwwkew\"", "3", "Substring with duplicates inside", false),
                        new TestCase("\"\"", "0", "Empty string", true)
                ),
                Arrays.asList("Sliding Window", "Hash Table", "String")
        ));

        // 9. Valid Anagram (Frequency Counting)
        challenges.add(new CodingChallenge(
                "COD-009",
                "Valid Anagram",
                "Data Structures & Algorithms",
                Difficulty.EASY,
                "Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`, and `false` otherwise.\nAn Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.\n\nExample 1:\nInput: s = \"anagram\", t = \"nagaram\"\nOutput: true\n\nExample 2:\nInput: s = \"rat\", t = \"car\"\nOutput: false",
                "s: string, t: string",
                "true if t is an anagram of s, false otherwise.",
                "1 <= s.length, t.length <= 5 * 10^4\ns and t consist of lowercase English letters.",
                "public class Solution {\n    public boolean isAnagram(String s, String t) {\n        if (s.length() != t.length()) return false;\n        int[] count = new int[26];\n        for (int i = 0; i < s.length(); i++) {\n            count[s.charAt(i) - 'a']++;\n            count[t.charAt(i) - 'a']--;\n        }\n        for (int c : count) {\n            if (c != 0) return false;\n        }\n        return true;\n    }\n}",
                "Frequency array of size 26: increment count for characters in s, decrement for characters in t. If all counts return to 0, they are anagrams. O(N) time, O(1) space.",
                Arrays.asList(
                        new TestCase("s = \"anagram\", t = \"nagaram\"", "true", "Valid anagrams", false),
                        new TestCase("s = \"rat\", t = \"car\"", "false", "Not an anagram", false),
                        new TestCase("s = \"a\", t = \"a\"", "true", "Single identical character", false),
                        new TestCase("s = \"ab\", t = \"a\"", "false", "Different lengths", true)
                ),
                Arrays.asList("Hash Table", "String", "Frequency Counting")
        ));

        return challenges;
    }
}
