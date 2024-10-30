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
                border-collapse: collapse;
                margin: 20px 0;
                font-size: 16px;
                text-align: left;
                box-shadow: 0 2px 15px rgba(0, 0, 0, 0.2);
                border-radius: 12px;
                overflow: hidden;
            }
            th, td {
                padding: 12px 15px;
                border: 1px solid #bbb;  /* Bordure entre les cellules */
            }
            th {
                background: linear-gradient(135deg, #007bff, #0056b3);
                color: white;
                font-weight: bold;
            }
            tr:nth-child(even) {
                background-color: #f2f2f2;  /* Lignes paires */
            }
            tr:hover {
                background-color: #e3f2fd;  /* Survol */
                transition: 0.3s;
            }
            .high {
                background-color: #f8d7da;  /* Rouge pâle pour les niveaux élevés */
                color: #721c24;
                font-weight: bold;
            }
            .medium {
                background-color: #fff3cd;  /* Orange pâle pour les niveaux moyens */
                color: #856404;
                font-weight: bold;
            }
            .low {
                background-color: #d4edda;  /* Vert pâle pour les niveaux faibles */
                color: #155724;
                font-weight: bold;
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
            # Ajouter une classe CSS en fonction de la gravité
            severity_class = "low"
            if vuln.get('Severity') == "HIGH":
                severity_class = "high"
            elif vuln.get('Severity') == "MEDIUM":
                severity_class = "medium"

            html_content += f"""
            <tr class="{severity_class}">
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
