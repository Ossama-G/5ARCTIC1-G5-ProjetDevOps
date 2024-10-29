import json
import sys

def json_to_html(json_file, html_file):
    with open(json_file, 'r') as f:
        data = json.load(f)

    with open(html_file, 'w') as f:
        f.write('<html><head><title>Trivy Vulnerability Report</title></head><body>')
        f.write('<h1>Trivy Vulnerability Report</h1>')
        for result in data.get('Results', []):
            f.write(f"<h2>Target: {result.get('Target')}</h2>")
            if 'Vulnerabilities' in result:
                f.write('<table border="1"><tr><th>Vulnerability ID</th><th>Severity</th><th>Description</th></tr>')
                for vuln in result['Vulnerabilities']:
                    f.write(f"<tr><td>{vuln.get('VulnerabilityID')}</td><td>{vuln.get('Severity')}</td><td>{vuln.get('Title')}</td></tr>")
                f.write('</table>')
        f.write('</body></html>')

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python3 json_to_html.py <input_json> <output_html>")
    else:
        json_to_html(sys.argv[1], sys.argv[2])
