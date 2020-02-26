import pandas as pd
from person import Person


def create_people():
    people = [
        Person('Chandler', 'Bing', 'Friends'),
        Person('Willow', 'Rosenberg', 'Buffy', 17),
        Person('Monica', 'Geller', 'Friends'),
        Person('Arya', 'Stark', 'Game of Thrones', 9),
        Person('Ross', 'Geller', 'Friends'),
        Person('Buffy', 'Summers', 'Buffy', 17),
        Person('Olenna', 'Tyrell', 'Game of Thrones', 50),
        Person('Rachel', 'Green', 'Friends'),
        Person('Sansa', 'Stark', 'Game of Thrones', 14),
        Person('Rupert', 'Giles', 'Buffy', 47),
        Person('Catelyn', 'Stark', 'Game of Thrones', 40),
        Person('Joey', 'Tribbiani', 'Friends'),
        Person('Tara', 'Maclay', 'Buffy', 17),
        Person('Margaery', 'Tyrell', 'Game of Thrones', 17),
        Person('Phoebe', 'Buffay', 'Friends'),
        Person('Xander', 'Harris', 'Buffy', 17),
    ]

    return pd.DataFrame.from_records([person_obj.to_dict() for person_obj in people],
                                     index=[f'person_{i}' for i, _ in enumerate(people)])


def output(value, title=''):
    if title:
        print(f'{title.title()}\n{"_" * 40}')
    print(value)
    print('\n')


def create_full_names(row: pd.Series):
    row['full_name'] = f'{row.first_name} {row.last_name}'
    return row


def show_df(people, show_all=False):
    if show_all:
        output(people, title='People')
    else:
        output(people.head(), title='head')


def get_values(people):
    show_df(people)
    output(people.loc[:, ['first_name', 'last_name']], title='names')
    output(people.loc[people.team == 'Friends'], title='Get Friends')
    output(people.loc[(people.team == 'Game of Thrones') & (people.last_name == 'Stark')], title='Get Starks from GoT')
    output(people.loc[people.first_name.map(lambda first_name: first_name.startswith('R'))],
           title='Get Characters with first_name starting with "R"')


def value_counts(people):
    output(people.team.value_counts(), title='value_counts usage (team)')
    output(people.team.value_counts(normalize=True), title='value_counts usage (team, normalized)')
    output(people.age.value_counts(normalize=True), title='value_counts usage (age, normalized)')


def get_mean(people):
    """ used with numbers """
    output(people.mean(), title='mean')
    output(people.age.mean(), title='mean (age)')


def group_by(people):
    output(people.groupby('team'), title='Group by Team')
    output(people.groupby('team').team, title='Get Team Grouped by Team')
    output(people.groupby('team').team.count(), title='Count Teams')
    output(people.groupby('team').team.min(), title='Min Team')

    output(people.groupby('team').apply(lambda person: person.full_name[0]), title='Get first person on each team')
    # TODO group by containing last name
    output(people.groupby('last_name').full_name.agg([len, min, max]), title="Group by last_name aggs of len, min, max")


def rename_column(data_frame, original_name, updated_name):
    return data_frame.rename(columns={original_name: updated_name})


def handle_rare_categorical_values(people):
    freq = people.team.value_counts(normalize=True)
    map_by_freq = people.team.map(freq)
    res = people.team.mask(map_by_freq < 0.32, 'Other')
    people['team'] = res  # changes actual DF
    print('handle_rare_categorical_values (using "Other")')
    show_df(people, True)


def investigate_cardinality(people):
    team_cardinality = people['team'].unique()
    print(f'a total of {len(team_cardinality)} unique entries')
    output(team_cardinality, 'cardinality (number of unique entries)')


if __name__ == '__main__':
    people: pd.DataFrame = create_people()
    show_df(people)
    output(people.describe(), 'describe pd')
    output(people.last_name.describe(), 'describe last_name')

    get_values(people)

    people_full: pd.DataFrame = people.apply(create_full_names, axis=1)
    show_df(people_full)
    value_counts(people_full)
    get_mean(people_full)
    group_by(people_full)
    show_df(rename_column(people_full, 'team', 'TV Series'))
    show_df(people_full.isnull().any())
    s = people_full.dtypes == 'object'
    ss = list(s[s])
    sss = list(s[s].index)
    investigate_cardinality(people_full)

    handle_rare_categorical_values(people_full)
    print('\n\nEnd')
