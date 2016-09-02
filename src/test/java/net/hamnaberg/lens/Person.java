package net.hamnaberg.lens;

import javaslang.Tuple2;

class Person {
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
