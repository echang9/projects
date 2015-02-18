#python --version 2.7.5
#Running python SimpleDatabase.py will run the interperter.

from collections import Counter
import sys

class SimpleDatabase(object):
    def __init__(self):
        self.database = {}
        self.history = []

    def putKeyVal(self, name, value):
        if (value != None):
            self.database[name] = value
        else:
            del self.database[name]

    def SET(self, name, value):
        if self.history:
            if name in self.database and name not in self.history[-1]:
                self.history[-1][name] = self.database[name]
        self.putKeyVal(name, value)

    def GET(self, name):
        if name in self.database:
            print self.database[name]
        else:
            print "NULL"

    def UNSET(self, name):
        if name in self.database:
            self.SET(name, None)

    def NUMEQUALTO(self, value):
        c = Counter(self.database.values())
        print c[value]

    def BEGIN(self):
        self.history.append({})

    def COMMIT(self):
        if not self.history:
            print "NO TRANSACTION"
        self.history = []

    def ROLLBACK(self):
        if self.history:
            self.database = self.history.pop()
        else:
            print "NO TRANSACTION"

if __name__ == "__main__":
    db = SimpleDatabase()
    line = sys.stdin.readline().strip()
    while line != 'END':
        inp = line.split(' ')
        cmd = inp[0]
        args = inp[1:]
        if hasattr(db, cmd):
            f = getattr(db, cmd);
            try:
                f(*args)
            except TypeError as e:
                print "Incorrect number of arguments."
        else:
            print "Unknown command"
            
        line = sys.stdin.readline().strip()
