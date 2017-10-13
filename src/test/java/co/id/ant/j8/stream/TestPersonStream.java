package co.id.ant.j8.stream;

import co.id.ant.j8.entity.Person;
import co.id.ant.j8.util.PersonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TestPersonStream {

  private List<Person> people;

  @Before
  public void loadTestData() throws IOException{

    byte[] files= Files.readAllBytes(Paths.get("src/test/resources/MOCK_DATA.json"));
    String arrayOfPeople = new String(files, StandardCharsets.UTF_8);

    Gson gson = new Gson();
    TypeToken<List<Person>> typeOfListPerson = new TypeToken<List<Person>>(){};
    people = gson.fromJson(arrayOfPeople, typeOfListPerson.getType());

  }

  @Test
  public void testFindPersonWithFirstNameStartFromA(){

    people
        .stream()
        .filter(aPerson -> {return aPerson.getFirstName().startsWith("A");})
        .limit(5l)
        .forEach(PersonUtil::printFirstName);
  }

}
