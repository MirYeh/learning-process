import pandas as pd
from choosing_features_and_label import check_columns, investigate_data, get_columns_to_exclude, get_X_and_y
from preprocess_data import scale_data, binarize_data


BASE_PATH = './resources'

PATH_TO_TEST_DATA = f'{BASE_PATH}/test.csv'
PATH_TO_TRAIN_DATA = f'{BASE_PATH}/train.csv'
PATH_TO_DATA = PATH_TO_TRAIN_DATA


def main():
    houses_df = pd.read_csv(PATH_TO_DATA)
    check_columns(houses_df)
    investigate_data(houses_df)
    exclude_columns = get_columns_to_exclude(houses_df)
    X, y = get_X_and_y(houses_df, 'SalePrice', exclude_columns)
    print(f'\n{"-" * 90}\n\nX:\n{X.columns} \nTotal of {len(X.columns)} features\n\ny:\n{y}')

    binarize_data(X)


if __name__ == '__main__':
    main()
