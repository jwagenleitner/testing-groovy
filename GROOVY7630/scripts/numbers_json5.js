// npm install json5

var JSON5 = require('json5');

var obj = JSON5.parse('{"num": 1a}');

// ^^^ SyntaxError: Expected '}' instead of 'a'

console.log(obj.num);
