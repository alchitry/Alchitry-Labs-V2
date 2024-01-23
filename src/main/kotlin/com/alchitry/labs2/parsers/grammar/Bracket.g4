grammar Bracket;

// starting rule
source: block* EOF?;

block: parenBlock | squareBlock | curlyBlock | commentBlock;
parenBlock: '(' block* ')';
squareBlock: '[' block* ']';
curlyBlock: '{' block* '}';
commentBlock: '/*' .*? '*/';

COMMENT: ('//' ~[\r\n]*) -> channel(HIDDEN);
WS: [ \t\n]+ -> skip;
JUNK: ~[[({})\]] -> channel(HIDDEN);