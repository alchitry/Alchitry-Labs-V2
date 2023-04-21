grammar Test;

startingRule: 'test' newLine;
unusedBadRule: startingRule '}';
newLine: '\n'+ | EOF;