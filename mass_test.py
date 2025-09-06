import requests
import csv
import uuid

def csv_to_list_of_dicts(filename):
    data = []
    with open(filename, 'r', newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            data.append(row)
    return data

outgoing_payments = csv_to_list_of_dicts("./payments.csv")

for outgoing_payment in outgoing_payments:
    success = False
    if not 'idempotency-key' in outgoing_payment:
        outgoing_payment['idempotency-key'] = str(uuid.uuid4())
    while not success:
        headers = {
            "Payment-Idempotence-Key": outgoing_payment['idempotency-key']
        }
        payload = {
            "src"   : outgoing_payment['src'],
            "dest"  : outgoing_payment['dest'],
            "amount": outgoing_payment['amount']
        }
        try:
            response = requests.post('http://localhost:8888/payments/', json=payload, headers=headers)
            if 199 < response.status_code < 300:
                success = True
        except Exception as e:
            print("fail...")