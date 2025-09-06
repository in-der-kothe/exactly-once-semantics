import requests
import csv

def csv_to_list_of_dicts(filename):
    data = []
    with open(filename, 'r', newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            data.append(row)
    return data

outgoing_payments = csv_to_list_of_dicts("./payments.csv")

for outgoing_payment in outgoing_payments:
    payload = {
        "src"   : outgoing_payment['src'],
        "dest"  : outgoing_payment['dest'],
        "amount": outgoing_payment['amount']
    }
    requests.post('http://localhost:8888/payments/', json=payload)
