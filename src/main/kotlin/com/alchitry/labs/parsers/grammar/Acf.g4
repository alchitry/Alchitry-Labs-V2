grammar Acf;

// starting rule
source: (pin | clock | NL)* EOF?;

pin: 'pin' portName pinName (PULLUP|PULLDOWN)? semi;

clock: 'clock' portName pinName (PULLUP|PULLDOWN)? frequency semi;

name: (BASIC_NAME | FREQ_UNIT);

portName: name arrayIndex* ('.' name arrayIndex*)*;
pinName: name;
frequency: number FREQ_UNIT;

arrayIndex: '[' INT ']';

number : INT | REAL;

PULLUP: 'pullup';
PULLDOWN: 'pulldown';

semi: NL | (NL* SEMICOLON) | EOF;

SEMICOLON : ';';
NL : '\r' '\n' | '\n' | '\r';

FREQ_UNIT: (M | K | G)? H Z;
BASIC_NAME: [a-zA-Z][a-zA-Z0-9_]*;
REAL: [0-9]* '.' [0-9]+ | [0-9]+ '.' [0-9]*;
INT: [0-9]+;

BLOCK_COMMENT: ('/*' .*? '*/') -> channel(HIDDEN);
COMMENT: ('//' ~[\r\n]*) -> channel(HIDDEN);
WS: [ \t]+ -> skip;

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');

