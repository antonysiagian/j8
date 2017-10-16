package co.id.ant.j8.stream;

import co.id.ant.j8.entity.Person;
import co.id.ant.j8.entity.PersonFullNameAndEmail;
import co.id.ant.j8.util.PersonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestPersonStream {

  private static List<Person> people = new ArrayList<Person>();
  private static List<Person> peopleWhoAreLessThan25YearsOld;
  private static List<Person> peopleWhoAreBetween25YearsOldUpTo50YearOld;
  private static List<Person> peopleWhoAreBetween50YearsOldUpTo100YearsOld;

  @Before
  public void loadTestData() throws IOException{

    if(people.isEmpty()) {
      byte[] files = Files.readAllBytes(Paths.get("src/test/resources/MOCK_DATA.json"));
      String arrayOfPeople = new String(files, StandardCharsets.UTF_8);

      Gson gson = new Gson();
      TypeToken<List<Person>> typeOfListPerson = new TypeToken<List<Person>>() {
      };

      people = gson.fromJson(arrayOfPeople, typeOfListPerson.getType());

      peopleWhoAreLessThan25YearsOld =
          people
              .stream()
              .filter((aPerson)-> {
                return aPerson.getAge() < 25;})
              .collect(Collectors.toList());


      peopleWhoAreBetween25YearsOldUpTo50YearOld =
          people
              .stream()
              .filter((aPerson)-> {
                int age = aPerson.getAge();
                return age > 24 && age < 50;})
              .collect(Collectors.toList());

      peopleWhoAreBetween50YearsOldUpTo100YearsOld =
          people
              .stream()
              .filter((aPerson)-> {
                int age = aPerson.getAge();
                return age > 49 && age < 100;})
              .collect(Collectors.toList());
    }

  }


  @Test
  public void testFindPersonWithFirstNameStartFromA(){

    List<Person> peopleWhoisNameStartWithA = people
        .stream()
        .filter(aPerson -> {
          return aPerson.getFirstName().startsWith("A");
        })
        .collect(Collectors.toList());

    peopleWhoisNameStartWithA.forEach(person -> Assert.assertTrue(person.getFirstName().startsWith("A")));

  }


  @Test
  public void testRangeWithPersonStream(){

    //range = exclusive.
    IntStream.range(0, 10).forEach(index->{
      Person person = people.get(index);
      PersonUtil.printPersonInJsonFormat(person);
    });

    //rangeClosed = inclusive
    IntStream.rangeClosed(0, 10).forEach(index -> {
      Person person = people.get(index);
      PersonUtil.printPersonInJsonFormat(person);
    });

  }


  @Test
  public void testFindingSmallestIdOfPeople(){

    int mininumIdOfPeople = people
        .stream()
        .mapToInt((aPerson)->{return aPerson.getId();})
        .min()
        .getAsInt();

    Assert.assertEquals(1, mininumIdOfPeople);

  }


  @Test
  public void testFindingBiggestIdofPeople(){

    int maxIdOfPeople = people.stream()
        .mapToInt((aPerson) -> {return aPerson.getId();})
        .max()
        .getAsInt();

    Assert.assertEquals(1000, maxIdOfPeople);
  }


  @Test
  public void testDisctinctUsingStream(){

    List<Person> listOfDuplicatePerson = new ArrayList<Person>();
    listOfDuplicatePerson.addAll(people);
    listOfDuplicatePerson.addAll(people);

    Assert.assertEquals(2000, listOfDuplicatePerson.size());

    //using distinct function
    List<Person> listOfUniquePerson = listOfDuplicatePerson
        .stream()
        .distinct()
        .collect(Collectors.toList());

    Assert.assertEquals(1000, listOfUniquePerson.size());

    //using collectors
    Set<Person> setOfUniquePerson = listOfDuplicatePerson
        .stream()
        .collect(Collectors.toSet());

    Assert.assertEquals(1000, setOfUniquePerson.size());
  }


  @Test
  public void testMapOnPersonStreamFromPersonToPersonFullNameAndEmail(){

    List<PersonFullNameAndEmail> listOfPeopleWithFullnameAndEmail =  people
        .stream()
        .map(PersonFullNameAndEmail::new)
        .collect(Collectors.toList());

    listOfPeopleWithFullnameAndEmail.forEach(PersonUtil::printPersonFullnameAndEmailInJsonFormat);

    listOfPeopleWithFullnameAndEmail.forEach(
        aPerson-> Assert.assertNotNull(aPerson.getFullNameAndEmail())
    );

  }


  @Test
  public void testAverageAgeOfpeople(){

    double averageAgeOfPeople = people
        .stream().mapToInt((aPerson)->{return aPerson.getAge();})
        .average()
        .getAsDouble();

    System.out.println(averageAgeOfPeople);
    Assert.assertTrue(45.949 == averageAgeOfPeople);

  }


  @Test
  public void testGatheringStatisticsOfStream(){

    IntSummaryStatistics statistics = people
        .stream()
        .mapToInt((aPerson)->{return aPerson.getAge();})
        .summaryStatistics();

    Assert.assertTrue( 45.949 == statistics.getAverage());
    Assert.assertEquals(1000, statistics.getCount());
    Assert.assertEquals(45949, statistics.getSum());
    Assert.assertEquals( 16, statistics.getMin());
    Assert.assertEquals( 77, statistics.getMax());

  }


  @Test
  public void testGroupingPeopleByAge(){

    Map<Integer, List<Person>> mapOfPeopleByAge = people
        .stream()
        .collect(Collectors.groupingBy(Person::getAge));

    Assert.assertEquals(62, mapOfPeopleByAge.keySet().size());

    //print the grouping result
    mapOfPeopleByAge.forEach(
        (age, listOfPersonByAge)->{
          listOfPersonByAge.forEach((aPerson)->{
            System.out.println(
                "Person with age "
                    .concat(String.valueOf(age)).concat(":")
                    .concat(PersonUtil.getJsonFormatFromPerson(aPerson)));
          });
        }
    );
  }


  @Test
  public void testGetTotalAgeUsingReduce(){

    int totalAgeOfPeople = people.stream()
        .mapToInt((aPerson) -> {
          return aPerson.getAge();
        })
        .reduce(0, (a, b) -> {
          return a + b;
        });

    Assert.assertEquals(45949, totalAgeOfPeople);

  }
  

  @Test
  public void testIntermediateAndTerminalOperation(){

    List<Person> youngPeople = people.stream().filter(
        (aPerson) -> {
          System.out.println(
              "when is this text printout?"
          ); //when you execute terminal operation (calling 'collect')
          return aPerson.getAge() < 20;
        }
    ).collect(Collectors.toList());

    youngPeople.forEach(aPerson-> Assert.assertTrue(aPerson.getAge() <  20 ));

  }


  @Test
  public void testFlatMapToJoinListOfPersonList() {

    //let's create a list consist of list people who already grouped to three groups
    List<List<Person>> listOfPeople = new ArrayList<List<Person>>();
    listOfPeople.add(TestPersonStream.peopleWhoAreLessThan25YearsOld);
    listOfPeople.add(TestPersonStream.peopleWhoAreBetween25YearsOldUpTo50YearOld);
    listOfPeople.add(TestPersonStream.peopleWhoAreBetween50YearsOldUpTo100YearsOld);

    List<Person> resultOfJoiningList = listOfPeople
        .stream()
        .flatMap((aGroupOfPeople) -> aGroupOfPeople.stream()).collect(Collectors.toList());

    Assert.assertEquals(1000, resultOfJoiningList.size());

  }


  @Test
  public void testPrintPersonIdUsingParallelStream(){

    people.parallelStream().parallel()
        .forEach((aPerson) -> {
          PersonUtil.printPersonInJsonFormat(aPerson);
    });

  }

  @Test
  public void testComparingPerformanceBetweenParallelStreamAndCasualStream(){

    //create inclusive list from 0  to 10000000
    List<Integer> numbers = IntStream.range(0, 10000000).boxed().collect(Collectors.toList());

    long beforeExecuteCasualStream = System.nanoTime();
    int totalNumberFromCasualStream = numbers.stream().reduce(0, Integer::sum);
    long executionTimeForCasualStream = System.nanoTime()  - beforeExecuteCasualStream;


    long beforeExecuteParallelStream = System.nanoTime();
    int totalNumberFromParallelStream = numbers.parallelStream().reduce(0, Integer::sum);
    long executionTimeForParallelStream = System.nanoTime()  - beforeExecuteParallelStream;

    System.out.println(
        "Execution time from casual stream: " + executionTimeForCasualStream +
            ", parallel stream:" + executionTimeForParallelStream);

    //assert that parallel stream is faster comparing to casual stream to execute computation
    Assert.assertTrue(executionTimeForCasualStream  > executionTimeForParallelStream);

  }

  @Test
  public void testJoiningStingUsingStream(){

    List<String>  listOfString = Arrays.asList("He", "She", "is", "a", "beautiful", "girl");

    String resultOfJoiningString = listOfString
        .stream()
        .filter(aString -> !aString.equals("He"))
        .collect(Collectors.joining(" "));

    Assert.assertEquals("She is a beautiful girl", resultOfJoiningString);

  }


  @Test
  public void testUsingOptionalWhenConditionIsNotFulfilled(){
    people.stream()
        .filter(aPerson -> aPerson.getAge() > 200)
        .mapToInt(
            (aPerson)-> {return aPerson.getAge()}
        );
  }

}