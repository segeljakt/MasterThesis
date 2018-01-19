# Rust BNF

From [this page](https://raw.githubusercontent.com/rust-lang/rust/master/src/doc/grammar.md).

## Comments

    comment : block_comment | line_comment ;
    block_comment : "/*" block_comment_body * "*/" ;
    block_comment_body : [block_comment | character] * ;
    line_comment : "//" non_eol * ;

## Whitespace

    whitespace_char : '\x20' | '\x09' | '\x0a' | '\x0d' ;
    whitespace : [ whitespace_char | comment ] + ;

## Tokens

    simple_token : keyword | unop | binop ;
    token : simple_token | ident | literal | symbol | whitespace token ;

### Keywords

    | _      | abstract | alignof | as       | become   |
    | box    | break    | const   | continue | crate    |
    | do     | else     | enum    | extern   | false    |
    | final  | fn       | for     | if       | impl     |
    | in     | let      | loop    | macro    | match    |
    | mod    | move     | mut     | offsetof | override |
    | priv   | proc     | pub     | pure     | ref      |
    | return | Self     | self    | sizeof   | static   |
    | struct | super    | trait   | true     | type     |
    | typeof | unsafe   | unsized | use      | virtual  |
    | where  | while    | yield   |          |          |

### Literals

    lit_suffix : ident;
    literal : [ string_lit | char_lit | byte_string_lit | byte_lit | num_lit | bool_lit ] lit_suffix ?;

#### Character and string literals

    char_lit : '\x27' char_body '\x27' ;
    string_lit : '"' string_body * '"' | 'r' raw_string ;

    char_body : non_single_quote
              | '\x5c' [ '\x27' | common_escape | unicode_escape ] ;

    string_body : non_double_quote
                | '\x5c' [ '\x22' | common_escape | unicode_escape ] ;
    raw_string : '"' raw_string_body '"' | '#' raw_string '#' ;

    common_escape : '\x5c'
                  | 'n' | 'r' | 't' | '0'
                  | 'x' hex_digit 2
    unicode_escape : 'u' '{' hex_digit+ 6 '}';

    hex_digit : 'a' | 'b' | 'c' | 'd' | 'e' | 'f'
              | 'A' | 'B' | 'C' | 'D' | 'E' | 'F'
              | dec_digit ;
    oct_digit : '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' ;
    dec_digit : '0' | nonzero_dec ;
    nonzero_dec: '1' | '2' | '3' | '4'
              | '5' | '6' | '7' | '8' | '9' ;

#### Byte and byte string literals

    byte_lit : "b\x27" byte_body '\x27' ;
    byte_string_lit : "b\x22" string_body * '\x22' | "br" raw_byte_string ;

    byte_body : ascii_non_single_quote
              | '\x5c' [ '\x27' | common_escape ] ;

    byte_string_body : ascii_non_double_quote
                | '\x5c' [ '\x22' | common_escape ] ;
    raw_byte_string : '"' raw_byte_string_body '"' | '#' raw_byte_string '#' ;

#### Number literals

    num_lit : nonzero_dec [ dec_digit | '_' ] * float_suffix ?
            | '0' [       [ dec_digit | '_' ] * float_suffix ?
                  | 'b'   [ '1' | '0' | '_' ] +
                  | 'o'   [ oct_digit | '_' ] +
                  | 'x'   [ hex_digit | '_' ] +  ] ;

    float_suffix : [ exponent | '.' dec_lit exponent ? ] ? ;

    exponent : ['E' | 'e'] ['-' | '+' ] ? dec_lit ;
    dec_lit : [ dec_digit | '_' ] + ;

#### Boolean literals

    bool_lit : [ "true" | "false" ] ;

### Symbols

    symbol : "::" | "->"
          | '#' | '[' | ']' | '(' | ')' | '{' | '}'
          | ',' | ';' ;

## Paths

    expr_path : [ "::" ] ident [ "::" expr_path_tail ] + ;
    expr_path_tail : '<' type_expr [ ',' type_expr ] + '>'
                  | expr_path ;

    type_path : ident [ type_path_tail ] + ;
    type_path_tail : '<' type_expr [ ',' type_expr ] + '>'
                  | "::" type_path ;

# Syntax extensions

## Macros

    expr_macro_rules : "macro_rules" '!' ident '(' macro_rule * ')' ';'
                    | "macro_rules" '!' ident '{' macro_rule * '}' ;
    macro_rule : '(' matcher * ')' "=>" '(' transcriber * ')' ';' ;
    matcher : '(' matcher * ')' | '[' matcher * ']'
            | '{' matcher * '}' | '$' ident ':' ident
            | '$' '(' matcher * ')' sep_token? [ '*' | '+' ]
            | non_special_token ;
    transcriber : '(' transcriber * ')' | '[' transcriber * ']'
                | '{' transcriber * '}' | '$' ident
                | '$' '(' transcriber * ')' sep_token? [ '*' | '+' ]
                | non_special_token ;

# Crates and source files

# Items and attributes

## Items

    item : vis ? mod_item | fn_item | type_item | struct_item | enum_item
        | const_item | static_item | trait_item | impl_item | extern_block_item ;

### Type Parameters

### Modules

    mod_item : "mod" ident ( ';' | '{' mod '}' );
    mod : [ view_item | item ] * ;

#### View items

    view_item : extern_crate_decl | use_decl ';' ;

##### Extern crate declarations

    extern_crate_decl : "extern" "crate" crate_name
    crate_name: ident | ( ident "as" ident )

##### Use declarations

    use_decl : vis ? "use" [ path "as" ident
                            | path_glob ] ;

    path_glob : ident [ "::" [ path_glob
                              | '*' ] ] ?
              | '{' path_item [ ',' path_item ] * '}' ;

    path_item : ident | "self" ;

### Functions

#### Generic functions

#### Unsafety

##### Unsafe functions

##### Unsafe blocks

#### Diverging functions

### Type definitions

### Structures

### Enumerations

### Constant items

    const_item : "const" ident ':' type '=' expr ';' ;

### Static items

    static_item : "static" ident ':' type '=' expr ';' ;

#### Mutable statics

### Traits

### Implementations

### External blocks

    extern_block_item : "extern" '{' extern_block '}' ;
    extern_block : [ foreign_fn ] * ;

## Visibility and Privacy

    vis : "pub" ;

### Re-exporting and Visibility

## Attributes

    attribute : '#' '!' ? '[' meta_item ']' ;
    meta_item : ident [ '=' literal
                      | '(' meta_seq ')' ] ? ;
    meta_seq : meta_item [ ',' meta_seq ] ? ;

# Statements and expressions

## Statements

    stmt : decl_stmt | expr_stmt | ';' ;

### Declaration statements

    decl_stmt : item | let_decl ;

#### Item declarations

#### Variable declarations

    let_decl : "let" pat [':' type ] ? [ init ] ? ';' ;
    init : [ '=' ] expr ;

### Expression statements

    expr_stmt : expr ';' ;

## Expressions

    expr : literal | path | tuple_expr | unit_expr | struct_expr
        | block_expr | method_call_expr | field_expr | array_expr
        | idx_expr | range_expr | unop_expr | binop_expr
        | paren_expr | call_expr | lambda_expr | while_expr
        | loop_expr | break_expr | continue_expr | for_expr
        | if_expr | match_expr | if_let_expr | while_let_expr
        | return_expr ;

#### Lvalues, rvalues and temporaries

#### Moved and copied types

### Literal expressions

### Path expressions

### Tuple expressions

    tuple_expr : '(' [ expr [ ',' expr ] * | expr ',' ] ? ')' ;

### Unit expressions

    unit_expr : "()" ;

### Structure expressions

    struct_expr_field_init : ident | ident ':' expr ;
    struct_expr : expr_path '{' struct_expr_field_init
                          [ ',' struct_expr_field_init ] *
                          [ ".." expr ] '}' |
                  expr_path '(' expr
                          [ ',' expr ] * ')' |
                  expr_path ;

### Block expressions

    block_expr : '{' [ stmt | item ] *
                    [ expr ] '}' ;

### Method-call expressions

    method_call_expr : expr '.' ident paren_expr_list ;

### Field expressions

    field_expr : expr '.' ident ;

### Array expressions

    array_expr : '[' "mut" ? array_elems? ']' ;

    array_elems : [expr [',' expr]*] | [expr ';' expr] ;

### Index expressions

    idx_expr : expr '[' expr ']' ;

### Range expressions

    range_expr : expr ".." expr |
                expr ".." |
                ".." expr |
                ".." ;

### Unary operator expressions

    unop_expr : unop expr ;
    unop : '-' | '*' | '!' ;

### Binary operator expressions

    binop_expr : expr binop expr | type_cast_expr
              | assignment_expr | compound_assignment_expr ;
    binop : arith_op | bitwise_op | lazy_bool_op | comp_op

#### Arithmetic operators

    arith_op : '+' | '-' | '*' | '/' | '%' ;

#### Bitwise operators

    bitwise_op : '&' | '|' | '^' | "<<" | ">>" ;

#### Lazy boolean operators

    lazy_bool_op : "&&" | "||" ;

#### Comparison operators

    comp_op : "==" | "!=" | '<' | '>' | "<=" | ">=" ;

#### Type cast expressions

    type_cast_expr : value "as" type ;

#### Assignment expressions

    assignment_expr : expr '=' expr ;

#### Compound assignment expressions

    compound_assignment_expr : expr [ arith_op | bitwise_op ] '=' expr ;

### Grouped expressions

    paren_expr : '(' expr ')' ;

### Call expressions

    expr_list : [ expr [ ',' expr ]* ] ? ;
    paren_expr_list : '(' expr_list ')' ;
    call_expr : expr paren_expr_list ;

### Lambda expressions

    ident_list : [ ident [ ',' ident ]* ] ? ;
    lambda_expr : '|' ident_list '|' expr ;

### While loops

    while_expr : [ lifetime ':' ] ? "while" no_struct_literal_expr '{' block '}' ;

### Infinite loops

    loop_expr : [ lifetime ':' ] ? "loop" '{' block '}';

### Break expressions

    break_expr : "break" [ lifetime ] ?;

### Continue expressions

    continue_expr : "continue" [ lifetime ] ?;

### For expressions

    for_expr : [ lifetime ':' ] ? "for" pat "in" no_struct_literal_expr '{' block '}' ;

### If expressions

    if_expr : "if" no_struct_literal_expr '{' block '}'
              else_tail ? ;

    else_tail : "else" [ if_expr | if_let_expr
                      | '{' block '}' ] ;

### Match expressions

    match_expr : "match" no_struct_literal_expr '{' match_arm * '}' ;

    match_arm : attribute * match_pat "=>" [ expr "," | '{' block '}' ] ;

    match_pat : pat [ '|' pat ] * [ "if" expr ] ? ;

### If let expressions

    if_let_expr : "if" "let" pat '=' expr '{' block '}'
                  else_tail ? ;

### While let loops

    while_let_expr : [ lifetime ':' ] ? "while" "let" pat '=' expr '{' block '}' ;

### Return expressions

    return_expr : "return" expr ? ;

# Type system

## Types

### Primitive types

#### Machine types

#### Machine-dependent integer types

### Textual types

### Tuple types

### Array, and Slice types

### Structure types

### Enumerated types

### Pointer types

### Function types

### Closure types

    closure_type := [ 'unsafe' ] [ '<' lifetime-list '>' ] '|' arg-list '|'
                    [ ':' bound-list ] [ '->' type ]
    lifetime-list := lifetime | lifetime ',' lifetime-list
    arg-list := ident ':' type | ident ':' type ',' arg-list

### Never type

    never_type : "!" ;

### Object types

### Type parameters

### Type parameter bounds

    bound-list := bound | bound '+' bound-list '+' ?
    bound := ty_bound | lt_bound
    lt_bound := lifetime
    ty_bound := ty_bound_noparen | (ty_bound_noparen)
    ty_bound_noparen := [?] [ for<lt_param_defs> ] simple_path

### Self types

## Type kinds

# Memory and concurrency models

## Memory model

### Memory allocation and lifetime

### Memory ownership

### Variables

### Boxes

## Threads

### Communication between threads

### Thread lifecycle
