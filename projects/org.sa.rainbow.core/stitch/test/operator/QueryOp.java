/**
 * Created April 26, 2006
 */
package test.operator;

/**
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 *
 * Operator class for issuing queries.
 */
public class QueryOp {

	public static void print (Object o) {
		System.out.println(o.toString());
	}

	public static void testFailure () {
		throw new RuntimeException("Ahhhhhhhhhhh!");
	}
}
