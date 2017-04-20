package net.hamnaberg.lens;

import javaslang.Tuple2;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.Gen;
import javaslang.test.Property;
import net.hamnaberg.lens.laws.IsoLaws;
import org.junit.Assert;
import org.junit.Test;


public class IsoTest {

    @Test
    public void id() {
        Iso<Tuple2<String, Integer>, Tuple2<String, Integer>> iso = Iso.identity();

        Arbitrary<Integer> ageA = Arbitrary.integer();
        Arbitrary<String> nameA = Arbitrary.string(Gen.choose('a', 'Z'));
        CheckResult result = Property.def("Identity Iso").
                forAll(nameA, ageA).
                suchThat((name, age) -> new IsoLaws<>(iso).satisfyAll(new Tuple2<>(name, age), new Tuple2<>(name, age))).
                check();

        result.assertIsSatisfied();
    }

    @Test
    public void personTest() {
        Iso<Person, Tuple2<String, Integer>> iso = Iso.of(Person::tupled, Person::fromTuple);
        Arbitrary<Integer> ageA = Arbitrary.integer();
        Arbitrary<String> nameA = Arbitrary.string(Gen.choose('a', 'Z'));

        CheckResult result = Property.def("PersonIso").
                forAll(nameA, ageA).
                suchThat((name, age) -> new IsoLaws<>(iso).satisfyAll(new Person(name, age), new Tuple2<>(name, age))).
                check();
        result.assertIsSatisfied();
    }

    @Test
    public void intWrapper() {
        Arbitrary<Integer> integer = Arbitrary.integer();
        //Arbitrary<IntWrapper> intWrapper = integer.map(IntWrapper::new);
        Iso<Integer, IntWrapper> iso = Iso.of(IntWrapper::new, IntWrapper::get);

        CheckResult result = Property.def("Integer wrapper iso").
                forAll(integer).
                suchThat((i) -> new IsoLaws<>(iso).satisfyAll(i, new IntWrapper(i))).
                check();
        result.assertIsSatisfied();


        Assert.assertTrue(new IsoLaws<>(iso).satisfyAll(23, new IntWrapper(23)));
    }

    private static class IntWrapper {
        private Integer wrapper;

        public IntWrapper(Integer wrapper) {
            this.wrapper = wrapper;
        }

        public int get() {
            return wrapper;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IntWrapper that = (IntWrapper) o;

            return wrapper.equals(that.wrapper);

        }

        @Override
        public int hashCode() {
            return wrapper;
        }

        @Override
        public String toString() {
            return "IntWrapper{" +
                    "wrapper=" + wrapper +
                    '}';
        }
    }
}
