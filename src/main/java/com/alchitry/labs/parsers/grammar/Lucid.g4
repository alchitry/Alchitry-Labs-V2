grammar Lucid;

@header {
package com.alchitry.labs.parsers.lucidv2.grammar;
}

// starting rule
source: (global | module | testBench | NL)* EOF;

global: 'global' NL* name NL* '{' NL* globalStat* NL* '}';

globalStat
  : structDec
  | constDec
  | enumDec
  ;

module: 'module' NL* name NL* paramList? NL* portList NL* moduleBody;
testBench: 'testBench' NL* name NL* moduleBody;

paramList: '#(' NL* paramDec (NL* ',' NL* paramDec)* NL* ')';
portList: '(' (NL* portDec (NL* ',' NL* portDec)*)? NL* ')';

paramDec: name (NL* '=' NL* paramDefault)? (NL* ':' NL* paramConstraint)?;
paramDefault: expr;
paramConstraint: expr;

portDec: (SIGNED NL*)? portDirection NL* name signalWidth;
portDirection: 'input'|'output'|'inout';

signalWidth: arraySize* structType?;

arraySize: '[' NL* expr NL* ']';
structType: '<' NL* name NL* ('.' NL* name NL*)? '>';
structMemberConst: '.' name NL* '(' NL* expr NL* ')';
structConst: structType NL* '(' NL* structMemberConst NL* (',' NL* structMemberConst NL*)* ')';

moduleBody: '{' (stat | NL)* '}';

stat
  : constDec       #StatConst
  | sigDec         #StatSig
  | enumDec        #StatEnum
  | dffDec         #StatDFF
  | moduleInst     #StatModuleInst
  | assignBlock    #StatAssign
  | alwaysBlock    #StatAlways
  | structDec      #StatStruct
  | testBlock      #StatTest
  | functionBlock  #StatFunction
  ;

constDec: 'const' NL* name NL* '=' NL* expr semi;

assignBlock: conList NL* '{' (dffDec | moduleInst | assignBlock | NL)* '}';
sigCon: '.' name NL* '(' NL* expr NL* ')';
paramCon: '#' name NL* '(' NL* expr NL* ')';

sigDec: (SIGNED NL*)? 'sig' NL* name signalWidth (NL* '=' NL* expr)? semi;
dffDec: (SIGNED NL*)? 'dff' NL* name signalWidth (NL* instCons)? semi;
enumDec: 'enum' NL* name NL* '{' NL* name (NL* ',' NL* name)* NL* '}' semi;

moduleInst: name NL* name (NL* arraySize)* (NL* instCons)? semi;

instCons : '(' NL* connection (NL* ',' NL* connection)* NL* ')';
conList  : connection (NL* ',' NL* connection)*;
connection: paramCon | sigCon;

structMember: (SIGNED NL*)? name signalWidth;
structDec: 'struct' NL* name NL* '{' NL* structMember (NL* ',' NL* structMember)* NL* '}' semi;

functionArg: name signalWidth;
functionBlock: 'fun' NL* name NL* '(' (NL* functionArg (NL* ',' NL* functionArg)*)? NL* ')' block;
testBlock: 'test' NL* name NL* block;

alwaysBlock: 'always' NL* block;

alwaysStat
  : assignStat     #AlwaysAssign
  | caseStat       #AlwaysCase
  | ifStat         #AlwaysIf
  | repeatStat     #AlwaysRepeat
  | function       #AlwaysFunction
  ;

block
  : '{' (NL | alwaysStat)* '}'
  | alwaysStat
  ;

assignStat: signal NL* '=' NL* expr semi;

arrayIndex: '[' NL* expr NL* ']';
bitSelector
  : '[' NL* expr NL* ':' NL* expr NL* ']'            #BitSelectorConst
  | '[' NL* expr NL* ('+'|'-') NL* ':' NL* expr NL* ']'  #BitSelectorFixWidth
  ;
bitSelection: (arrayIndex | NL)* (arrayIndex | bitSelector);

signal: name (NL* bitSelection)? (NL* '.' NL* name (NL* bitSelection)?)*;

caseStat: 'case' NL* '(' NL* expr NL* ')' NL* '{' (caseElem | NL)* '}';
caseElem: (expr | 'default') NL* ':' caseBlock;
caseBlock: (alwaysStat | NL)* alwaysStat;

ifStat: 'if' NL* '(' NL* expr NL* ')' NL* block (NL* elseStat)?;
elseStat: 'else' NL* block;

repeatStat: 'repeat' NL* '(' NL* expr NL* (',' NL* name NL*)? ')' NL* repeatBlock;
repeatBlock: block;

function: FUNCTION_ID NL* '(' (NL* functionExpr (NL* ',' NL* functionExpr)*)? NL* ')';
functionExpr: expr | REAL;

number: HEX | BIN | DEC | INT | STRING;

expr
  : signal                                          #ExprSignal
  | number                                          #ExprNum
  | structConst                                     #ExprStruct
  | function                                        #ExprFunction
  | '(' NL* expr NL* ')'                            #ExprGroup
  | 'c{' NL* expr (NL* ',' NL* expr)* NL* '}'       #ExprConcat
  | expr NL* 'x{' NL* expr NL* '}'                  #ExprDup
  | '{' NL* expr (NL* ',' NL* expr)* NL* '}'        #ExprArray
  | ('~'|'!') NL* expr                              #ExprInvert
  | '-' NL* expr                                    #ExprNegate
  | expr NL* ('*'|'/') NL* expr                     #ExprMultDiv
  | expr NL* ('+'|'-') NL* expr                     #ExprAddSub
  | expr NL* ('>>'|'<<'|'<<<'|'>>>') NL* expr       #ExprShift
  | expr NL* ('|'|'&'|'^') NL* expr                 #ExprBitwise
  | ('|'|'&'|'^') NL* expr                          #ExprReduction
  | expr NL* ('<'|'>'|'=='|'!='|'>='|'<=') NL* expr #ExprCompare
  | expr NL* ('||'|'&&') NL* expr                   #ExprLogical
  | expr NL* '?' NL* expr NL* ':' NL* expr          #ExprTernary
  ;

name: TYPE_ID | CONST_ID | SPACE_ID;

semi: NL | (NL* SEMICOLON);

HEX: ([1-9][0-9]*)? 'h' ([0-9a-fA-FzZX]|('x' {_input.LA(1) != '{'}?))+;
BIN: ([1-9][0-9]*)? 'b' ([0-1zZX]|('x' {_input.LA(1) != '{'}?))+;
DEC: ([1-9][0-9]*)? 'd' [0-9]+;
REAL: '-'? [0-9]* '.' [0-9]+ | '-'? [0-9]+ '.' [0-9]*;
INT: [0-9]+;
STRING: '"' ( '\\' ~[\r\n] | ~[\\"\r\n] )* '"';
SEMICOLON : ';';
NL : '\r' '\n' | '\n' | '\r';

SIGNED: 'signed';
TYPE_ID: [a-z]([a-wy-zA-Z0-9_]|('x' {_input.LA(1) != '{'}?))*;
CONST_ID: [A-Z][A-Z0-9_]*;
SPACE_ID: [A-Z]([a-wy-zA-Z0-9_]|('x' {_input.LA(1) != '{'}?))*;
FUNCTION_ID: '$'[a-z][a-zA-Z0-9_]*;

BLOCK_COMMENT: ('/*' .*? '*/') -> channel(HIDDEN);
COMMENT: ('//' ~[\r\n]*) -> channel(HIDDEN);
WS: [ \t]+ -> skip;