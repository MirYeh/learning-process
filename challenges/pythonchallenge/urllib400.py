from urllib import request

url = 'http://www.pythonchallenge.com/pc/def/pickle.html'

response = request.urlopen(url)
sentence = response.read().split()
print('sentence: ', sentence)
next_nothing = sentence[-1]
try:
	nothing = int(next_nothing)
except:
	nothing = input('enter next number: ')
