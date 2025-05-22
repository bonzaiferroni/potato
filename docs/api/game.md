game.entities (EntityMap):
```kt
fun EntityMap.readEntity<T: Entity>(): T
fun EntityMap.readEntity<T: Entity>(id: Long): T?
fun EntityMap.readWithState<T: EntityState>(id: Long): StateEntity<T>?
fun EntityMap.readWithState<T: EntityState>(predicate: (StateEntity<T>) -> Boolean): StateEntity<T>?
fun EntityMap.readWithZone<T>(zone: Zone): T?
fun EntityMap.readWithZone<T>(zoneId: Int): T?
fun EntityMap.findNearest<T: Entity>(position: Position, range: Float = Float.MAX_VALUE, isTarget: (T) -> Boolean): T?
fun EntityMap.sumOf<S: EntityState>(block: (S) -> Double): Double
fun EntityMap.sumOf<S: EntityState>(where: (StateEntity<S>) -> Boolean, block: (S) -> Double): Double
fun EntityMap.count(block: (Entity) -> Boolean): Int
fun EntityMap.count(kClass: KClass<*>, zoneId: Int): Int
fun EntityMap.count<E: StateEntity<*>>(): Int
```
