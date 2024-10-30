import json
import sys

def json_to_html(json_file, html_file):
    # Charger le fichier JSON
    with open(json_file, 'r') as f:
        data = json.load(f)

    # Début du document HTML
    html_content = """
    <html>
    <head>
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
            #download-button {
                margin: 20px;
                padding: 12px 24px;
                background: linear-gradient(135deg, #28a745, #218838);
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-size: 18px;
                font-weight: bold;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
                transition: background 0.3s, box-shadow 0.3s;
            }
            #download-button:hover {
                background: linear-gradient(135deg, #218838, #1e7e34);
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
            }
            #zip-link {
                margin-bottom: 10px;
                display: inline-block;
                font-size: 18px;
                color: #007bff;
                text-decoration: none;
            }
            #zip-link:hover {
                text-decoration: underline;
            }
        </style>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.9.2/html2pdf.bundle.min.js"></script>
    </head>
    <body>
        <h1>Trivy Vulnerability Report</h1>
        <a href="#" id="zip-link">Zip</a>
        <button id="download-button" onclick="downloadPDF()">PDF</button>
    """

    # Ajouter les détails des vulnérabilités
    for result in data.get("Results", []):
        html_content += f"<h2>Target: {result.get('Target')}</h2>"
        html_content += "<table>"
        html_content += "<tr><th>Vulnerability ID</th><th>Severity</th><th>Description</th></tr>"

        for vuln in result.get("Vulnerabilities", []):
            severity_color = "#dc3545" if vuln.get('Severity') == "HIGH" else "#ffc107"
            html_content += f"""
            <tr>
                <td>{vuln.get('VulnerabilityID')}</td>
                <td style="color: {severity_color}; font-weight: bold;">{vuln.get('Severity')}</td>
                <td>{vuln.get('Title')}</td>
            </tr>
            """

        html_content += "</table>"

    # Ajouter le script pour le téléchargement en PDF
    html_content += """
        <script>
            function downloadPDF() {
                const element = document.body;
                html2pdf().from(element).save('trivy-vulnerability-report.pdf');
            }
        </script>
    </body>
    </html>
    """

    # Enregistrer dans le fichier HTML
    with open(html_file, 'w') as f:
        f.write(html_content)

# Récupérer les arguments de ligne de commande (fichier JSON et fichier HTML)
if len(sys.argv) != 3:
    print("Usage: python json_to_html.py <input_json> <output_html>")
    sys.exit(1)

json_file = sys.argv[1]
html_file = sys.argv[2]

# Appeler la fonction pour convertir JSON en HTML
json_to_html(json_file, html_file)
