bayes-dota
==========

This is the [task](TASK.md).


####Dependencies added:
- `'org.jetbrains', name: 'annotations', version: '19.0.0'` - used to design contracts (`@NotNull`, `@Nullable`)

####Further ideas:
- add unique constraint to table `message` to avoid that the same message can enter the database twice.
Unique constraint on table `message`: `(matchId, heroName, durationInMillis)` does not work, 
since there are different messages with the same set of columns. 
Maybe composite of `matchId`, `durationInMillis`, `heroName` and a hash value of the whole object works?
- analyze if entity structure is ok. Is inheritance appropriate? There are four different strategies 
 according to https://www.baeldung.com/hibernate-inheritance. Is single table inheritance strategy 
 best for this use case?
 (read https://thoughts-on-java.org/complete-guide-inheritance-strategies-jpa-hibernate/).
 I an not a fan of inheritance - I rarely use it if at all. I would like to reevaluate if it makes sense
 in this context of message hierarchy...
- analyze if db-indexes are needed.
- add smoke tests


####Run all tests
`mvn clean verify`

####Run application
`mvn clean spring-boot:run`
