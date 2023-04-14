class Poly {
    class Term {
        private int coef;
        private int expo;
        private Term next;
        private Term(int coef, int expo, Term next) {
            this.coef = coef;
            this.expo = expo;
            this.next = next;
        }
    }
    private Term first;
    private Term last;
    public Poly() {
        Term head = new Term(0, Integer.MAX_VALUE, null);
        first = head;
        last = head;
    }
    public boolean isZero() {
        return first.next == null;
    }
    public Poly minus() {
        Poly result = new Poly();
        Term temp = first.next;
        while (temp != null) {
            result.last.next = new Term(-temp.coef, temp.expo, null);
            result.last = result.last.next;
            temp = temp.next;
        }
        return result;
    }
    public Poly plus(Poly that) {
        Poly result = new Poly();
        Term left = first.next;
        Term right = that.first.next;
        while (left != null && right != null) {
            if (left.expo > right.expo) {
                result.last.next = new Term(left.coef, left.expo, null);
                result.last = result.last.next;
                left = left.next;
            }
            else if (right.expo > left.expo) {
                result.last.next = new Term(right.coef, right.expo, null);
                result.last = result.last.next;
                right = right.next;
            }
            else if (left.coef + right.coef != 0) {
                result.last.next = new Term(left.coef + right.coef, left.expo, null);
                result.last = result.last.next;
                left = left.next;
                right = right.next;
            }
            else {
                left = left.next;
                right = right.next;
            }
        }
        if (left != null) {
            result.last.next = left;
        }
        else if (right != null){
            result.last.next = right;
        }
        return result;
    }
    public Poly plus(int coef, int expo) {
        if (coef == 0 || expo < 0 || expo >= last.expo) {
            throw new IllegalArgumentException("Cannot add this term to the polynomial.");
        }
        else {
            last.next = new Term(coef, expo, null);
            last = last.next;
        }
        return this;
    }
    public String toString() {
        StringBuilder out = new StringBuilder();
        Term temp = first.next;
        if (isZero()) {
            return "0";
        }
        else {
            out.append(temp.coef + "x");
            appendExpo(out, temp.expo);
            temp = temp.next;
        }
        if (temp.next != null && temp.next.coef >= 0) {
            out.append(" + ");
        }
        else if (temp.next != null) {
            out.append(" - " );
        }
        while(temp != null) {
            if (temp.coef > 0) {
                out.append(temp.coef + "x");
                appendExpo(out, temp.expo);
            }
            else if (temp.coef < 0){
                out.append((-temp.coef) + "x");
                appendExpo(out, temp.expo);
            }
            if (temp.next != null && temp.next.coef >= 0) {
                out.append(" + ");
            }
            else if (temp.next != null) {
                out.append(" - " );
            }
            temp = temp.next;
        }
        return out.toString();
    }


    // APPEND EXPO. Append superscript digits for EXPO to BUILDER. EXPO is greater
// than or equal to zero.
    private void appendExpo(StringBuilder builder, int expo)
    {
        if (expo == 0)
        {
            builder.append('⁰');
        }
        else
        {
            appendingExpo(builder, expo);
        }
    }
    // APPENDING EXPO. Do all the work for APPEND EXPO.
    private void appendingExpo(StringBuilder builder, int expo)
    {
        if (expo > 0)
        {
            appendingExpo(builder, expo / 10);
            builder.append("⁰¹²³⁴⁵⁶⁷⁸⁹".charAt(expo % 10));
        }
    }
}

class Driver {
    public static void main(String[] args) {
        Poly p = new Poly().plus(3,5).plus(2,4).plus(2,3).plus(-1,2).plus(5,0);
        Poly q = new Poly().plus(7,4).plus(1,2).plus(-4,1).plus(-3,0);
        Poly z = new Poly();

        System.out.println(p);                 // 3x⁵ + 2x⁴ + 2x³ - 1x² + 5x⁰
        System.out.println(q);                 // 7x⁴ + 1x² - 4x¹ - 3x⁰
        System.out.println(z);                 // 0

        System.out.println(p.minus());         // -3x⁵ - 2x⁴ - 2x³ + 1x² - 5x⁰
        System.out.println(q.minus());         // -7x⁴ - 1x² + 4x¹ + 3x⁰
        System.out.println(z.minus());         // 0

        System.out.println(p.plus(q));         // 3x⁵ + 9x⁴ + 2x³ - 4x¹ + 2x⁰
        System.out.println(p.plus(z));         // 3x⁵ + 2x⁴ + 2x³ - 1x² + 5x⁰
        System.out.println(p.plus(q.minus())); // 3x⁵ - 5x⁴ + 2x³ - 2x² + 4x¹ + 8x⁰
        System.out.println(p.plus(q).plus(z));
        System.out.println(p.plus(z).plus(q));
    }
}


