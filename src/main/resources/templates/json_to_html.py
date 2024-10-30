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
                font-family: 'Arial', sans-serif;
                background-color: #f8f9fa;
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
            }
            table {
                width: 80%;
                border-collapse: collapse;
                margin: 20px 0;
                font-size: 16px;
                text-align: left;
                box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
            }
            th, td {
                border: 1px solid #ddd;  /* Ajouter des bordures */
                padding: 12px 15px;
            }
            th {
                background-color: #343a40;
                color: #fff;
                border-right: 1px solid #bbb;  /* Bordure verticale entre les colonnes */
            }
            td {
                border-right: 1px solid #bbb;  /* Bordure verticale entre les colonnes */
            }
            tr:nth-child(even) {
                background-color: #f2f2f2;
            }
            tr:hover {
                background-color: #d6e9c6;
                transition: 0.2s;
            }
        </style>
    </head>
    <body>
        <h1>Trivy Vulnerability Report</h1>
    """

    # Ajouter les détails des vulnérabilités
    for result in data.get("Results", []):
        html_content += f"<h2>Target: {result.get('Target')}</h2>"
        html_content += "<table>"
        html_content += "<tr><th>Vulnerability ID</th><th>Severity</th><th>Description</th></tr>"

        for vuln in result.get("Vulnerabilities", []):
            html_content += f"""
            <tr>
                <td>{vuln.get('VulnerabilityID')}</td>
                <td>{vuln.get('Severity')}</td>
                <td>{vuln.get('Title')}</td>
            </tr>
            """

        html_content += "</table>"

    # Fin du document HTML
    html_content += """
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
