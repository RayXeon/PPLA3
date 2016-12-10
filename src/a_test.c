// In the assignment we state that we don't do macros,
// and the problem you run into is that we can not include
// things like "stdio.h" as a result. So here are some
// prototypes that at least get the job done.

int printf(const char *s, ...);
int strcmp(const char *s, const char *d);

#define SECOND 100
#define FIRST SECOND

#define A_FILE "./src/a_file.h"
#include A_FILE

static const char *strdown( char *foo)
{
    static char temp[ 50 ];
    char *scan = temp;
    *scan++ = *foo++; /* Copy first letter */
    while ( *foo )
        if ( *foo >= 'A' && *foo <= 'Z' )
            *scan++ = *foo++ PLUS TWENTY
        else
            *scan++ = FOO_PLUS_PLUS;
    return( temp );
}


int main( int ac, char *av[] )
{
    static char first[] = {'F', 'I', 'R', 'S', 'T', 0x00 };
    static char *oops = "#define this that";

    printf("Starting #define test...\n");
    // Should substitute for FIRST and then for SECOND
    if ( FIRST == 100 )
        printf("Correct!\n");
    else
        printf("Incorrect!\n");

    /* Check this one out... */
    if ( ! strcmp( "#define this that", oops ) )
        printf("Correct!\n");
    else
        printf("Incorrect!\n");

    
    // Should NOT substitute within a string
    if ( ! strcmp("FIRST", first) )
        printf("Correct!\n");
    else
        printf("Incorrect!\n");

    // Include test
    printf("Starting include test...\n");
#include A_FILE
    if ( INCLUDED_OK )
        printf("Correct!\n");
    else
        printf("Incorrect!\n");

    printf(STRING_OK+2);
    printf("%c%c%c%c%c%c%c%c%c",CHAR_1,CHAR_2,CHAR_3,
           CHAR_4,CHAR_5,CHAR_6,CHAR_7,CHAR_8,CHAR_9);
    printf(CMT_OUT);
    printf(C_FILE_INCLUDED);
    printf(strdown(D_FILE_INCLUDED));

    printf("Starting misc. tests...\n");
    // Other things. Comments, for instance.
    /*
      printf("Incorrect!\n");
    */
    printf("Correct!\n");
    printf( ISA );

#include "./src/e_file.h"

    return( 0 );
}

