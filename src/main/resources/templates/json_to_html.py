import json
import sys

if len(sys.argv) != 3:
    print("Usage: python json-to-html.py <input_json> <output_html>")
    sys.exit(1)

input_json = sys.argv[1]
output_html = sys.argv[2]

with open(input_json, 'r') as json_file:
    data = json.load(json_file)

html_content = '''
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trivy Vulnerability Report</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 80%;
            margin: auto;
            overflow: hidden;
        }
        h1 {
            text-align: center;
            margin-top: 20px;
            color: #444;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-size: 16px;
            text-align: left;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        table, th, td {
            border: 1px solid #ddd;
            padding: 12px;
        }
        th {
            background-color: #f2f2f2;
            color: #333;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .severity-CRITICAL {
            background-color: #ff6666;
            color: white;
        }
        .severity-HIGH {
            background-color: #ff9999;
            color: white;
        }
        .severity-MEDIUM {
            background-color: #ffcc66;
            color: black;
        }
        .severity-LOW {
            background-color: #99cc99;
            color: black;
        }
        .download-button {
            display: block;
            width: 200px;
            margin: 20px auto;
            padding: 10px;
            text-align: center;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            font-weight: bold;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .download-button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Trivy Vulnerability Report</h1>
    <table>
        <thead>
            <tr>
                <th>Vulnerability ID</th>
                <th>Severity</th>
                <th>Description</th>
            </tr>
        </thead>
        <tbody>
'''

for result in data.get("Results", []):
    for vulnerability in result.get("Vulnerabilities", []):
        html_content += f'''
            <tr class="severity-{vulnerability.get("Severity", "UNKNOWN")}">
                <td>{vulnerability.get("VulnerabilityID", "N/A")}</td>
                <td>{vulnerability.get("Severity", "N/A")}</td>
                <td>{vulnerability.get("Title", "N/A")}</td>
            </tr>
'''

html_content += '''
        </tbody>
    </table>
    <a href="trivy-fs-report.html" class="download-button" download>Download Report</a>
</div>
</body>
</html>
'''

with open(output_html, 'w') as html_file:
    html_file.write(html_content)
