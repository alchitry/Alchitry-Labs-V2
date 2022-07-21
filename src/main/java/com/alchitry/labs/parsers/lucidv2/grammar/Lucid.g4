grammar Lucid;

@header {
package com.alchitry.labs.parsers.lucidv2.grammar;
}

// starting rule
source: (global | module | NL)* EOF;

global: 'global' NL* name NL* '{' NL* global_stat* NL* '}';

global_stat
  : struct_dec 
  | const_dec
  ;

module: 'module' NL* name NL* param_list? NL* port_list NL* module_body;

param_list: '#(' NL* param_dec NL* (',' NL* param_dec NL*)* ')';
port_list: '(' NL* port_dec NL* (',' NL* port_dec NL*)* ')';

param_dec: param_name NL* (':' NL* param_constraint)?;
port_dec: input_dec | output_dec | inout_dec;

input_dec: SIGNED? NL* 'input' NL* struct_type? NL* name (array_size | NL)*;
output_dec: SIGNED? NL* 'output' NL* struct_type? NL* name (array_size | NL)*;
inout_dec: SIGNED? NL* 'inout' NL* struct_type? NL* name (array_size | NL)*;

param_name: name NL* ('=' NL* expr)?;
param_constraint: expr;

array_size: '[' NL* expr NL* ']';
struct_type: '<' NL* name NL* ('.' NL* name NL*)* '>';
struct_member_const: name NL* '(' NL* expr NL* ')';
struct_const: struct_type NL* '(' NL* struct_member_const NL* (',' NL* struct_member_const NL*)* ')';

module_body: '{' (stat | NL)* '}';

stat
  : const_dec       #StatConst
  | sig_dec         #StatSig
  | fsm_dec         #StatFSM
  | dff_dec         #StatDFF
  | module_inst     #StatModuleInst
  | assign_block    #StatAssign
  | always_block    #StatAlways
  | struct_dec      #StatStruct
  ;

const_dec: 'const' NL* name NL* '=' NL* expr NL* semi;

assign_block: con_list NL* '{' (dff_dec | fsm_dec | module_inst | assign_block | NL)* '}';
sig_con: '.' NL* name NL* '(' NL* expr NL* ')';
param_con: '#' NL* name NL* '(' NL* (expr | REAL) NL* ')';

type_dec: name (array_size | NL)*;
dff_single: name (array_size | NL)* inst_cons?;

sig_dec: SIGNED? NL* 'sig' NL* struct_type? NL* type_dec (NL* ',' NL* type_dec)* semi;
dff_dec: SIGNED? NL* 'dff' NL* struct_type? NL* dff_single (NL* ',' NL* dff_single)* semi;
fsm_dec: 'fsm' NL* name (array_size | NL)* inst_cons? NL* '=' NL* '{' NL* fsm_states NL* '}' semi;
fsm_states: name (NL* ',' NL* name)*;

module_inst: name NL* name (array_size | NL)* inst_cons? semi;

inst_cons : '(' NL* con_list NL* ')';
con_list  : connection (NL* ',' NL* connection)*;
connection: param_con | sig_con;

struct_member: SIGNED? NL* name NL* struct_type? (array_size | NL)*;
struct_dec: 'struct' NL* name NL* '{' NL* struct_member (NL* ',' NL* struct_member)* NL* '}' semi;

always_block: 'always' NL* block;

always_stat
  : assign_stat     #AlwaysStat
  | case_stat       #AlwaysCase
  | if_stat         #AlwaysIf
  | repeat_stat     #AlwaysRepeat
  ;

block
  : '{' NL* always_stat* NL* '}'
  | always_stat          
  ;

assign_stat: signal NL* '=' NL* expr semi;

array_index: '[' NL* expr NL* ']';
bit_selector
  : '[' NL* expr NL* ':' NL* expr NL* ']'            #BitSelectorConst
  | '[' NL* expr NL* ('+'|'-') NL* ':' NL* expr NL* ']'  #BitSelectorFixWidth
  ;
bit_selection: (array_index | NL)* (array_index | bit_selector);

signal: name NL* bit_selection? (NL* '.' NL* name NL* bit_selection?)*;

case_stat: 'case' NL* '(' NL* expr NL* ')' NL* '{' (case_elem | NL)* '}';
case_elem: (expr | 'default') NL* ':' NL* always_stat (always_stat | NL)*;

if_stat: 'if' NL* '(' NL* expr NL* ')' NL* block NL* else_stat?;
else_stat: 'else' NL* block;

repeat_stat: 'repeat' NL* '(' NL* expr NL* (':' NL* signal NL*) ')' NL* block;

function: FUNCTION_ID NL* '(' NL* expr (NL* ',' NL* expr)* NL* ')';

number: HEX | BIN | DEC | INT | STRING;

expr
  : signal                                          #ExprSignal
  | number                                          #ExprNum
  | struct_const                                    #ExprStruct
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

semi: NL+ | (NL* SEMICOLON NL*) | EOF;

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