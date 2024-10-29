import json
import sys

def json_to_html(json_file, html_file):
    with open(json_file, 'r') as f:
        data = json.load(f)

    html_content = """
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <title>Trivy Vulnerability Report</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #e0e0e0, #ffffff);
                color: #212529;
                margin: 0;
                padding: 0;
                display: flex;
                justify-content: center;
                align-items: center;
                flex-direction: column;
                min-height: 100vh;
            }
            h1 {
                color: #007bff;
                font-size: 2.5em;
                margin-bottom: 10px;
            }
            table {
                width: 90%;
                border-collapse: separate;
                border-spacing: 0;
                margin: 20px 0;
                font-size: 16px;
                text-align: left;
                box-shadow: 0 2px 15px rgba(0, 0, 0, 0.2);
                border-radius: 12px;
                overflow: hidden;
            }
            th, td {
                padding: 12px 15px;
                border-bottom: 1px solid #e0e0e0;
            }
            th {
                background: linear-gradient(135deg, #007bff, #0056b3);
                color: white;
                font-weight: bold;
            }
            tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            tr:hover {
                background-color: #e3f2fd;
                transition: 0.3s;
            }
        </style>
    </head>
    <body>
        <h1>Trivy Vulnerability Report</h1>
        <table>
            <tr><th>Vulnerability ID</th><th>Severity</th><th>Description</th></tr>
    """

    for result in data.get("Results", []):
        for vuln in result.get("Vulnerabilities", []):
            severity_color = "#dc3545" if vuln.get('Severity') == "HIGH" else "#ffc107"
            html_content += f"""
            <tr>
                <td>{vuln.get('VulnerabilityID')}</td>
                <td style="color: {severity_color}; font-weight: bold;">{vuln.get('Severity')}</td>
                <td>{vuln.get('Title')}</td>
            </tr>
            """

    html_content += """
        </table>
    </body>
    </html>
    """

    with open(html_file, 'w') as f:
        f.write(html_content)

if len(sys.argv) != 3:
    print("Usage: python json_to_html.py <input_json> <output_html>")
    sys.exit(1)

json_file = sys.argv[1]
html_file = sys.argv[2]

json_to_html(json_file, html_file)
