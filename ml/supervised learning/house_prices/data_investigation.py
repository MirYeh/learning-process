import traceback

import pandas as pd


def print_columns(houses_df: pd.DataFrame):
	print(f'houses_df.columns: {houses_df.columns}')
	print('\n' + '-' * 90 + '\n')
	__value_counts(houses_df, True)


def get_features(houses_df: pd.DataFrame, exclude_threshold=0.8):
	exclude_columns = __get_excluded_features(houses_df, exclude_threshold)
	return houses_df.drop(exclude_columns, axis=1)


def __get_excluded_features(houses_df: pd.DataFrame, exclude_threshold):
	exclude_columns = {'Id': 'removed'}
	for col in houses_df.columns:
		col_value_count: pd.Series = houses_df[col].value_counts(normalize=True, dropna=False)
		first_val = col_value_count.get(0)
		col_desc = houses_df[col].describe()

		if first_val and first_val > exclude_threshold:
			exclude_columns[col] = first_val
		else:
			count = col_desc['count']
			try:
				freq = col_desc['freq']/count
				if freq > exclude_threshold:
					exclude_columns[col] = freq
			except KeyError as e:
				pass
			except Exception:
				traceback.print_exc()

	return exclude_columns


def __value_counts(houses_df: pd.DataFrame, verbose=False):
	for col in houses_df.columns:
		col_value_counts = houses_df[col].value_counts(normalize=True, dropna=False)
		if verbose:
			print(f'\n\nColumn: {col}:')
			print(f'Total of {len(col_value_counts)} unique values:')
			print(col_value_counts)

