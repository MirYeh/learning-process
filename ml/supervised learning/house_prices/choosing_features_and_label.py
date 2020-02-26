from pprint import pprint
import pandas as pd


def check_columns(houses_df: pd.DataFrame):
    print(f'columns:\n{houses_df.columns}')
    print(len(houses_df.columns))


def investigate_data(houses_df: pd.DataFrame):
    print(f'head():\n{houses_df.head()}')
    print(f'describe:\n{houses_df.describe()}')
    print(f"unique YrSold: {pd.unique(houses_df['YrSold'])}")
    print(f"value_counts YrSold: \n{houses_df['YrSold'].value_counts()}")
    print(f"Example of value_counts on Street:\n{houses_df['Street'].value_counts(normalize=True, dropna=False)}")


def get_columns_to_exclude(houses_df: pd.DataFrame, exclude_threshold=0.8):
    exclude_columns = {'Id': 0}
    for col in houses_df.columns:
        print(f'\n\nColumn: {col}')
        col_value_count_with_nan: pd.Series = houses_df[col].value_counts(normalize=True, dropna=False)
        first_val = col_value_count_with_nan.get(0)
        col_desc = houses_df[col].describe()
        print(f'describe:\n{col_desc}')
        print(f'\ncol_value_count_with_nan:\n{col_value_count_with_nan}')

        if first_val and first_val > exclude_threshold:
            exclude_columns[col] = first_val
        else:
            count = col_desc['count']
            try:
                freq = col_desc['freq']/count
                if freq > exclude_threshold:
                    exclude_columns[col] = freq
            except:
                pass
        print('\n\n')

    print('exclude_columns:')
    pprint(exclude_columns)
    return exclude_columns


def get_X_and_y(houses_df: pd.DataFrame, label, exclude_columns: pd.Series):
    exclude_columns[label] = 0
    y = houses_df[label]
    X = houses_df.drop(exclude_columns, axis=1)
    return X, y
