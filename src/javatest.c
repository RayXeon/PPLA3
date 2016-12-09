
/*
This is obviously Java code and not C code. The problem is that if you
run "gcc -E" it will see that the filename ends in "java" and turn
aorund and run "gcj" instead. Of course "gcj" does not have a "-E"
option so thigs go bad. So sure, it says it is a C file but it
isn't. Deal with it.
*/

#define CORRECT "Correct!"
#define PAPER   CUT
#define CUT     GLUE
#define GLUE    OK
#define OK      true
#include "/home/wmahoney/CSCI4220/fall_three/d_file.h"
#define NO      !  // Ouch
#define define  "fine"
#define foolit  public static void func( int i ) { /* */ printer( i == ONE ); }
#define ONE     1

class MyClass {

    private static void printer( boolean flag ) {
        if ( flag )
            System.out.println( "Correct!" );
        else
            System.out.println( "Incorrect!" );
    }

    //  foolit
        foolit
    //  foolit

    public static void main(String[] args) {

        String x;

        if ( PAPER )
            printer( true );

        x = new String(CORRECT);
        printer( x.equals( CORRECT ) );

        // #define PLUS -
        printer( 2 PLUS 2 == 4 );

        x = new String( REAL + REAL );
        printer( NO x.equals( CORRECT ) );

        // CORRECT, we will CUT the PAPER and GLUE it back.
        x = new String( /* define PLUS */ define PLUS define );
        printer( x.equals( "finefine" ) );

        func(ONE);

        // One more and we're good.
        char [] tester = new char[] { 'C', 'O', 'R', 'R', 'E', 'C', 'T' };
        String comp = new String( tester );
        //               0123456789012345
        x = new String( "define what is CORRECT and what is just OK" );
        printer(x.indexOf(comp) == 15);
    }

}
