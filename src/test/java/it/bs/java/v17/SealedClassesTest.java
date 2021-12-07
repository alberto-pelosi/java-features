package it.bs.java.v17;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

class SealedClassesTest {

    /**
     * sealed classes (or interfaces) limit to only some classes the ability to extend (or implement) them
     */
    sealed interface Shape permits Circle, Quadrilateral, StrangeShape {
        BigDecimal perimeter();
    }

    /**
     * a class that implements a sealed interfaces can be final, so it can't be extended
     */
    static final class Circle implements Shape {
        private final BigDecimal radius;

        Circle(BigDecimal radius) {
            this.radius = radius;
        }

        @Override
        public BigDecimal perimeter() {
            return radius.multiply(BigDecimal.valueOf(2 * Math.PI));
        }
    }

    /**
     * a class that extends (or implements) a sealed class / interface can be a sealed class
     */
    sealed class Quadrilateral implements Shape permits Rectangle {
        private final BigDecimal l1, l2, l3, l4;

        Quadrilateral(BigDecimal l1, BigDecimal l2, BigDecimal l3, BigDecimal l4) {
            this.l1 = l1;
            this.l2 = l2;
            this.l3 = l3;
            this.l4 = l4;
        }

        @Override
        public BigDecimal perimeter() {
            return l1.add(l2).add(l3).add(l4);
        }
    }

    /**
     * a class that extends a sealed class, can be a normal class
     */
    sealed class Rectangle extends Quadrilateral permits Square {
        Rectangle(BigDecimal l1, BigDecimal l2) {
            super(l1, l2, l1, l2);
        }

    }

    final class Square extends Rectangle {

        Square(BigDecimal l) {
            super(l, l);
        }
    }

    non-sealed static class StrangeShape implements Shape {
        BigDecimal l1, l2, l3, l4, l5;

        StrangeShape(BigDecimal l1, BigDecimal l2, BigDecimal l3, BigDecimal l4, BigDecimal l5) {
            this.l1 = l1;
            this.l2 = l2;
            this.l3 = l3;
            this.l4 = l4;
            this.l5 = l5;
        }

        @Override
        public BigDecimal perimeter() {
            return l1.add(l2).add(l3).add(l4).add(l5);
        }
    }

    @Test
    void sealedClassesTest() {
        Shape circle = new Circle(BigDecimal.valueOf(3));
        Shape quadrilateral = new Quadrilateral(BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3), BigDecimal.valueOf(4));
        Shape rectangle = new Rectangle(BigDecimal.valueOf(1), BigDecimal.valueOf(2));
        Shape square = new Square(BigDecimal.valueOf(3));
        Shape strangeShape = new StrangeShape(BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3), BigDecimal.valueOf(4), BigDecimal.valueOf(5));
        assertEquals(BigDecimal.valueOf(18.84), circle.perimeter().setScale(2, RoundingMode.FLOOR));
        assertEquals(BigDecimal.valueOf(10.0), quadrilateral.perimeter().setScale(1, RoundingMode.FLOOR));
        assertEquals(BigDecimal.valueOf(6.0), rectangle.perimeter().setScale(1, RoundingMode.FLOOR));
        assertEquals(BigDecimal.valueOf(12.0), square.perimeter().setScale(1, RoundingMode.FLOOR));
        assertEquals(BigDecimal.valueOf(15.0), strangeShape.perimeter().setScale(1, RoundingMode.FLOOR));
    }

    @Test
    void patternMatchingTest() {
        Shape circle = new Circle(BigDecimal.valueOf(3));
        Shape quadrilateral = new Quadrilateral(BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3), BigDecimal.valueOf(4));
        Shape rectangle = new Rectangle(BigDecimal.valueOf(1), BigDecimal.valueOf(2));
        Shape square = new Square(BigDecimal.valueOf(3));
        Shape strangeShape = new StrangeShape(BigDecimal.valueOf(1), BigDecimal.valueOf(2), BigDecimal.valueOf(3), BigDecimal.valueOf(4), BigDecimal.valueOf(5));
        assertEquals("CIRCLE", patternMatching(circle));
        assertEquals("QUADRILATERAL", patternMatching(quadrilateral));
        assertEquals("RECTANGLE", patternMatching(rectangle));
        assertEquals("SQUARE", patternMatching(square));
        assertEquals("STRANGE-SHAPE", patternMatching(strangeShape));
    }

    private String patternMatching(Shape shape) {
        return switch (shape) {
            case Square ignored -> "SQUARE";
            case Rectangle ignored1 -> "RECTANGLE";
            case Quadrilateral ignored2 -> "QUADRILATERAL";
            case Circle ignored3 -> "CIRCLE";
            case StrangeShape ignored4 -> "STRANGE-SHAPE";
        };
    }
}
