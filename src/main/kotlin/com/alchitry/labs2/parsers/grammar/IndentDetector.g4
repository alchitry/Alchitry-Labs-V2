grammar IndentDetector;

// starting rule
source: (OPEN_BRACKET | CLOSED_BRACKET)* EOF?;

OPEN_BRACKET: [[({];
CLOSED_BRACKET: [\])}];


BLOCK_COMMENT: ('/*' .*? '*/') -> channel(HIDDEN);
COMMENT: ('//' ~[\r\n]*) -> channel(HIDDEN);
WS: [ \t\n]+ -> skip;
JUNK: ~[[({})\]] -> channel(HIDDEN);