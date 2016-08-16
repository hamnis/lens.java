package net.hamnaberg.lens;

import javaslang.Tuple2;
import javaslang.test.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;

public class IsoTests {

    public <S, A> void laws(Iso<S, A> iso, S value, A value2) {
        Function<S, S> oneWay = s -> iso.reverseGet(iso.get(s));
        Function<A, A> otherWay = a -> iso.get(iso.reverseGet(a));

        Assert.assertEquals(value, oneWay.apply(value));
        Assert.assertEquals(value2, otherWay.apply(value2));
    }

    @Test
    public void id() {
        Iso<Tuple2<String, Integer>, Tuple2<String, Integer>> iso = Iso.identity();

        laws(iso, new Tuple2<>("Hello", 1), new Tuple2<>("Hello", 1));
    }

    @Test
    public void personTest() {
        Iso<Person, Tuple2<String, Integer>> iso = new Iso<>(Person::tupled, Person::fromTuple);

        laws(iso, new Person("John doe", 32), new Tuple2<>("John doe", 32));
    }


    private static class Person {
        public final String name;
        public final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (age != person.age) return false;
            return name.equals(person.name);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + age;
            return result;
        }

        public Tuple2<String, Integer> tupled() {
            return new Tuple2<>(name, age);
        }

        public static Person fromTuple(Tuple2<String, Integer> t) {
            return new Person(t._1, t._2);
        }
    }
}
