package com.healthycoderapp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.Array;
import java.sql.SQLOutput;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BMICalculatorTest {

    private String environment = "prod";

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all unit tests.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all unit tests.");
    }

    static class isDietRecommendedTest {
        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110.0, 1.78"})
        void shouldReturnTrueWhenDietRecommended(Double coderWeight, Double coderHeight) {

            // given or arrange
            double weight = coderWeight;
            double height = coderHeight;

            // when or act
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // then or assert
            assertTrue(recommended);
        }
        @Test
        void shouldReturnFalseWhenDietNotRecommended() {

            // given or arrange
            double weight = 50.0;
            double height = 1.92;

            // when or act
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            // then or assert
            assertFalse(recommended);
        }
    }


    @Test
    void shouldThrowArithmeticExceptionWhenHeightZero() {

        // given or arrange
        double weight = 50.0;
        double height = 0;

        // when or act
        Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

        // then or assert
        assertThrows(ArithmeticException.class, executable);
    }

    @Test
    void shouldReturnCoderWithWorstBMIWhenCoderListNotEmpty() {

        // given or arrange
        List<Coder> coders = new ArrayList<>();
        coders.add(new Coder(1.80,60.0));
        coders.add(new Coder(1.82,98.0));
        coders.add(new Coder(1.82,64.7));

        // when or act
        Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

        // then or assert
        assertAll(
                () -> assertEquals(1.82, coderWorstBMI.getHeight()),
                () -> assertEquals(98.0, coderWorstBMI.getWeight())
        );
    }



    @Test
    void shouldReturnCoderWithWorstBMIIn1MsWhenCoderListHas10000Elements() {
        //given
        List<Coder> coders = new ArrayList<>();
        for (int i=0; i < 10000; i++) {
            coders.add(new Coder(1.0 + i, 10.0 + i));
        }

        //when
        Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

        //then
        assertTimeout(Duration.ofMillis(1), executable);
    }

    @Test
    void shouldReturnNullWorstBMICoderWhenCoderListEmpty() {

        // given or arrange
        assumeTrue(this.environment.equals("prod"));
        List<Coder> coders = new ArrayList<>();

        // when or act
        Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

        // then or assert
        assertNull(coderWorstBMI);
    }

    @Test
    void shouldReturnCorrectBMIScoreArrayWhenCoderListNotEmpty() {

        // given or arrange
        List<Coder> coders = new ArrayList<>();
        coders.add(new Coder(1.80,60.0));
        coders.add(new Coder(1.82,98.0));
        coders.add(new Coder(1.82,64.7));

        double[] expected = {18.52, 29.59, 19.53};
        // when or act
        double[] bmiScores = BMICalculator.getBMIScores(coders);

        // then or assert
        assertArrayEquals(expected, bmiScores);
    }
}