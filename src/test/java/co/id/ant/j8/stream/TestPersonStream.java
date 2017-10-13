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
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPersonStream {

  private static List<Person> people = new ArrayList<Person>();

  @Before
  public void loadTestData() throws IOException{

    if(people.isEmpty()) {
      byte[] files = Files.readAllBytes(Paths.get("src/test/resources/MOCK_DATA.json"));
      String arrayOfPeople = new String(files, StandardCharsets.UTF_8);

      Gson gson = new Gson();
      TypeToken<List<Person>> typeOfListPerson = new TypeToken<List<Person>>() {
      };
      people = gson.fromJson(arrayOfPeople, typeOfListPerson.getType());
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
        .map(
              (aPerson)->{
                return new PersonFullNameAndEmail(
                  aPerson.getFirstName(),
                  aPerson.getLastName(),
                  aPerson.getEmail());
              }
            )
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


}
