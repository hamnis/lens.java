package net.hamnaberg.lens;

import javaslang.Tuple2;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.Gen;
import javaslang.test.Property;
import org.junit.Test;

import java.util.function.Function;

public class IsoTests {

    public <S, A> boolean laws(Iso<S, A> iso, S value, A value2) {
        return value.equals(iso.reverseGet(iso.get(value))) && value2.equals(iso.get(iso.reverseGet(value2)));
    }

    public <S, A> boolean modify(Iso<S, A> iso, Function<A, A> f, S value, S expected) {
        return iso.modify(f).apply(value).equals(expected);
    }

    @Test
    public void id() {
        Iso<Tuple2<String, Integer>, Tuple2<String, Integer>> iso = Iso.identity();

        Arbitrary<Integer> ageA = Arbitrary.integer();
        Arbitrary<String> nameA = Arbitrary.string(Gen.choose('a', 'Z'));
        CheckResult result = Property.def("Identity Iso").
                forAll(nameA, ageA).
                suchThat((name, age) -> laws(iso, new Tuple2<>(name, age), new Tuple2<>(name, age))).
                check();

        result.assertIsSatisfied();

        CheckResult modifyResult = Property.def("Modify Iso").
                forAll(nameA, ageA).
                suchThat((name, age) -> modify(iso, Function.identity(), new Tuple2<>(name, age), new Tuple2<>(name, age))).
                check();

        modifyResult.assertIsSatisfied();

    }

    @Test
    public void personTest() {
        Iso<Person, Tuple2<String, Integer>> iso = new Iso<>(Person::tupled, Person::fromTuple);
        Arbitrary<Integer> ageA = Arbitrary.integer();
        Arbitrary<String> nameA = Arbitrary.string(Gen.choose('a', 'Z'));

        CheckResult result = Property.def("PersonIso").
                forAll(nameA, ageA).
                suchThat((name, age) -> laws(iso, new Person(name, age), new Tuple2<>(name, age))).
                check();
        result.assertIsSatisfied();
    }

    @Test
    public void intWrapper() {
        Arbitrary<Integer> integer = Arbitrary.integer();
        //Arbitrary<IntWrapper> intWrapper = integer.map(IntWrapper::new);
        Iso<Integer, IntWrapper> iso = new Iso<>(IntWrapper::new, IntWrapper::get);

        CheckResult result = Property.def("Integer wrapper iso").
                forAll(integer).
                suchThat((i) -> laws(iso, i, new IntWrapper(i))).
                check();
        result.assertIsSatisfied();


        laws(iso, 23, new IntWrapper(23));
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
