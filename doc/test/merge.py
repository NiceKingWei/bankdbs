while True:
    f = input()
    flag=True
    for line in ["<!DOCTYPE html>","<html>","<head>","<title></title>","</head>","<body>","</body>","</html>"]:
        if(f.find(line)!=-1):
            flag=False
            break
    if(flag):
        print(f)