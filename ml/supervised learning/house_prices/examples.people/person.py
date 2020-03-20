class Person:
    def __init__(self, first_name, last_name, team, age=30):
        self.first_name = first_name
        self.last_name = last_name
        self.team = team
        self.age = age

    def to_dict(self):
        return {
            'first_name': self.first_name,
            'last_name': self.last_name,
            'team': self.team,
            'age': self.age,
        }
