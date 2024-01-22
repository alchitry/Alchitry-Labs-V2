grammar IndentDetector;

// starting rule
source: (OPEN_BRACKET | CLOSED_BRACKET)* EOF?;

OPEN_BRACKET: [[({];
CLOSED_BRACKET: [\])}];

JUNK: ~[[({})\]]+ -> channel(HIDDEN);
BLOCK_COMMENT: ('/*' .*? '*/') -> channel(HIDDEN);
COMMENT: ('//' ~[\r\n]*) -> channel(HIDDEN);
WS: [ \t\n]+ -> skip;

