package co.id.ant.j8.util;

import co.id.ant.j8.entity.Person;
import com.google.gson.Gson;

public class PersonUtil {

  private static final Gson personInGson = new Gson();

  public static void printFirstName(Person person){
    System.out.println(person.getFirstName());
  }

  public static void printPersonInJsonFormat(Person person){
    System.out.println(personInGson.toJson(person));
  }
}
