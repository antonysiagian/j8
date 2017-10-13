package co.id.ant.j8.stream;

import co.id.ant.j8.entity.Person;
import co.id.ant.j8.util.PersonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
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

}
